package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.StudentRepository;
import com.example.jeeprojectspringboot.schoolmanager.Student;
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
public class StudentService {

	private final StudentRepository studentRepository;
	private final PersonService personService;
	private final MailService mailService;
	private final GradesService gradesService;

	@Autowired
	public StudentService(StudentRepository studentRepository, PersonService personService, MailService mailService, GradesService gradesService) {
		this.studentRepository = studentRepository;
		this.personService = personService;
		this.mailService = mailService;
		this.gradesService = gradesService;
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Student findStudentById(Long id) {
		return studentRepository.findById(id).orElse(null);
	}

	public List<Student> findStudentByClasseId(Long classeId) {
		return studentRepository.findByClasseId(classeId);
	}

	public List<Student> findStudentByPathwayId(Long pathwayId) {
		return studentRepository.findStudentsByPathway(pathwayId);
	}

	public List<Student> findStudentByPromoId(Long promoId) {
		return studentRepository.findStudentsByPromo(promoId);
	}

	@Transactional
	public Student saveStudent(Student student, boolean isNew, Long formerClasseId) {
		if (isNew) {
			personService.setPersonNumber(student);
			String password = String.format("%02d%02d%d", student.getBirthday().getDayOfMonth(), student.getBirthday().getMonthValue(), student.getBirthday().getYear());
			student.setPassword(password);
		}

		if (!isNew && formerClasseId == null) {
			throw new IllegalArgumentException("Student cannot already exists and have no former classe");
		}

		// set the username based on the name
		String username = "e-" + student.getFirstName().charAt(0);
		username = username.toLowerCase();
		String lastNameCut = student.getLastName().length() > 7 ? student.getLastName().substring(0, 7) : student.getLastName();
		lastNameCut = lastNameCut.toLowerCase();
		if (personService.getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
			int x = 1;
			while (personService.getUserByUsername(username + lastNameCut + x) != null) {
				x++;
			}
			student.setUsername(username + lastNameCut + x);
		} else {
			student.setUsername(username + lastNameCut);
		}

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Student>> violations = validator.validate(student);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		if (!isNew) {
			if (formerClasseId.equals(student.getClasse().getId())) { // TODO rajouter le nom de la classe quand le ClasseService sera terminé -> -> -> -> -> -> -> -> -> -> -> -> -> ->  <-
				mailService.sendEmail("do.not.reply@cytech.fr", student, "Changement dans vos inscription", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + /* classeManager.getClasseById(classeId).getName() + */ ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
			}
		}

		return studentRepository.save(student);
	}

	@Transactional
	public void deleteStudentById(Long id) {
		Student student = findStudentById(id);
		if (student != null) {
			gradesService.deleteGradesForStudent(student);
		}
		studentRepository.deleteById(id);
	}
}