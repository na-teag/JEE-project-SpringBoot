package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.StudentRepository;
import com.example.jeeprojectspringboot.schoolmanager.Person;
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

	private final  ClasseService classeService;

	@Autowired
	public StudentService(StudentRepository studentRepository, PersonService personService, MailService mailService, GradesService gradesService, ClasseService classeService) {
		this.studentRepository = studentRepository;
		this.personService = personService;
		this.mailService = mailService;
		this.gradesService = gradesService;
		this.classeService = classeService;
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
			personService.setPassword(student);
			personService.setUsername(student);
		}

		if (!isNew && formerClasseId == null) {
			throw new IllegalArgumentException("Student cannot already exists and have no former classe");
		}


		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Student>> violations = validator.validate(student);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Person otherUser = personService.emailExists(student.getEmail());
		if(otherUser != null && !otherUser.getId().equals(student.getId())) {
			throw new IllegalArgumentException("Cet email est déjà attribué");
		}

		if (!isNew) {
			if (!formerClasseId.equals(student.getClasse().getId())) {
				mailService.sendEmail("do.not.reply@cytech.fr", student, "Changement dans vos inscriptions", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + classeService.getClasse(student.getClasse().getId()).getName() + ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
			}
		} else {
			personService.sendAccountCreationMail(student);
			mailService.sendEmail("do.not.reply@cytech.fr", student, "Première inscription à CYTech", "Bonjour, Vous recevez cet email car vous venez d'être attribué à une nouvelle classe : " + classeService.getClasse(student.getClasse().getId()).getName() + ".\nConsultez votre emploi du temps pour voir vos nouveau cours.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
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