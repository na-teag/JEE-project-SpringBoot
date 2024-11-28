package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.ProfessorRepository;
import com.example.jeeprojectspringboot.schoolmanager.Professor;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ProfessorService {

	private final ProfessorRepository professorRepository;
	private final PersonService personService;
	private final CourseService courseService;

	@Autowired
	public ProfessorService(ProfessorRepository professorRepository, PersonService personService, CourseService courseService) {
		this.professorRepository = professorRepository;
		this.personService = personService;
		this.courseService = courseService;
	}

	public List<Professor> getAllProfessors() {
		return professorRepository.findAll();
	}

	public Professor findProfessorById(Long id) {
		return professorRepository.findById(id).orElse(null);
	}

	@Transactional
	public Professor saveProfessor(Professor professor, boolean isNew) {
		if (isNew) {
			personService.setPersonNumber(professor);
			String password = String.format("%02d%02d%d", professor.getBirthday().getDayOfMonth(), professor.getBirthday().getMonthValue(), professor.getBirthday().getYear());
			professor.setPassword(password);
		}

		// set the username based on the name
		String username = "e-" + professor.getFirstName().charAt(0);
		username = username.toLowerCase();
		String lastNameCut = professor.getLastName().length() > 7 ? professor.getLastName().substring(0, 7) : professor.getLastName();
		lastNameCut = lastNameCut.toLowerCase();
		if (personService.getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
			int x = 1;
			while (personService.getUserByUsername(username + lastNameCut + x) != null) {
				x++;
			}
			professor.setUsername(username + lastNameCut + x);
		} else {
			professor.setUsername(username + lastNameCut);
		}

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Professor>> violations = validator.validate(professor);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		return professorRepository.save(professor);
	}

	@Transactional
	public void deleteProfessorById(Long id) throws IllegalStateException {
		Professor professor = findProfessorById(id);
		if (professor != null) {
			if (!courseService.getCoursesOfProfessor(professor).isEmpty()){
				throw new IllegalStateException("il y a des cours donnés par le professeur " + professor.getFirstName() + " " + professor.getLastName() + " d'enregistrés, veuillez les associer à un autre professeur ou les supprimer auparavant");
			}
			// courseOccurrenceService.deleteByProfessor(professor); TODO utiliser CourseOccurrenceService quand il existera
		}
		professorRepository.deleteById(id);
	}
}