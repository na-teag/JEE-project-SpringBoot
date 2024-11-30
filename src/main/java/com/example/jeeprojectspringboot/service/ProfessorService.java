package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.ProfessorRepository;
import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Person;
import com.example.jeeprojectspringboot.schoolmanager.Professor;
import com.example.jeeprojectspringboot.schoolmanager.Subject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ProfessorService {

	private final ProfessorRepository professorRepository;
	private final PersonService personService;
	private final CourseService courseService;

	private final CourseOccurrenceService courseOccurrenceService;

	@Autowired
	public ProfessorService(ProfessorRepository professorRepository, PersonService personService, CourseService courseService, CourseOccurrenceService courseOccurrenceService) {
		this.professorRepository = professorRepository;
		this.personService = personService;
		this.courseService = courseService;
		this.courseOccurrenceService = courseOccurrenceService;
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
		} else {
			// vérifier qu'on a pas enlevé la permission d'enseigner des cours qu'il enseigne déjà
			List<Course> formerCourses = courseService.getCoursesOfProfessor(professor);
			List<Long> subjectIdsList = new ArrayList<>();
			if (!formerCourses.isEmpty()) {
				for (Subject subject : professor.getTeachingSubjects()) { // faire une liste pour comparer les id, sinon la comparaison est toujours fausse
					subjectIdsList.add(subject.getId());
				}
				for (Course course : formerCourses) {
					if (!subjectIdsList.contains(course.getSubject().getId())) {
						throw new IllegalStateException("Le professeur " + professor.getFirstName() + " " + professor.getLastName() + " enseigne déjà des cours de la matière " + course.getSubject().getName() + ". Veuillez supprimer ces cours ou leur assigner un autre professeur");
					}
				}
			}
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
		Person otherUser = personService.emailExists(professor.getEmail());
		if(otherUser != null && !otherUser.getId().equals(professor.getId())) {
			throw new IllegalArgumentException("Cet email est déjà attribué");
		}

		return professorRepository.save(professor);
	}

	@Transactional
	public void deleteProfessorById(Long id) throws IllegalStateException {
		Professor professor = findProfessorById(id);
		if (professor != null) {
			if (!professor.getTeachingSubjects().isEmpty()) {
				professor.setTeachingSubjects(new ArrayList<>());
				saveProfessor(professor, false);
			}
			if (!courseService.getCoursesOfProfessor(professor).isEmpty()) {
				throw new IllegalStateException("Il y a des cours enseignés par le professeur " + professor.getFirstName() + " " + professor.getLastName() + " d'enregistés.\nVeuillez supprimer ces cours, ou leur assigner un autre professeur");
			}
			courseOccurrenceService.deleteByProfessor(professor);
		}
		professorRepository.deleteById(id);
	}


}
