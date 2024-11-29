package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.schoolmanager.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataCleanupService {

	@Autowired
	private AddressService addressService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private ClassCategoryService classCategoryService;
	@Autowired
	private ClasseService classeService;
	@Autowired
	private CourseOccurrenceService courseOccurrenceService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private GradesService gradesService;
	@Autowired
	private PathwayService pathwayService;
	@Autowired
	ProfessorService professorService;
	@Autowired
	PromoService promoService;
	@Autowired
	StudentGroupService studentGroupService;
	@Autowired
	StudentService studentService;
	@Autowired
	SubjectService subjectService;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void deleteAllEntities() {

		// remove objects in intermediate tables
		List<Course> courses = courseService.getAllCourses();
		for (Course course : courses) {
			course.setStudentGroups(new ArrayList<>());
		}
		List<Professor> professors = professorService.getAllProfessors();
		for (Professor professor : professors) {
			professor.setTeachingSubjects(new ArrayList<>());
		}

		// Désactiver les vérifications de clé étrangère
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

		// Exécuter les suppressions des données dans chaque table
		entityManager.createQuery("DELETE FROM Address").executeUpdate();
		entityManager.createQuery("DELETE FROM Admin").executeUpdate();
		entityManager.createQuery("DELETE FROM ClassCategory").executeUpdate();
		entityManager.createQuery("DELETE FROM Classe").executeUpdate();
		entityManager.createQuery("DELETE FROM Course").executeUpdate();
		entityManager.createQuery("DELETE FROM CourseOccurrence").executeUpdate();
		entityManager.createQuery("DELETE FROM Grade").executeUpdate();
		entityManager.createQuery("DELETE FROM Pathway").executeUpdate();
		entityManager.createQuery("DELETE FROM Professor").executeUpdate();
		entityManager.createQuery("DELETE FROM Promo").executeUpdate();
		entityManager.createQuery("DELETE FROM Student").executeUpdate();
		entityManager.createQuery("DELETE FROM Subject").executeUpdate();

		// Réactiver les vérifications de clé étrangère
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
	}


	@Transactional
	public void fillDatabase() {
		// Address
		Address address = new Address();
		address.setNumber("1");
		address.setStreet("rue Lebon");
		address.setCity("Cergy");
		address.setPostalCode(95000);
		address.setCountry("France");
		addressService.addAddress(address);


		// Admin
		Admin admin = new Admin();
		admin.setFirstName("John");
		admin.setLastName("Doe");
		admin.setEmail("john.doe@cy-tech.fr");
		admin.setAddress(address);
		admin.setPassword("admin");
		admin.setUsername("admin");
		admin.setBirthday(LocalDate.of(2000,1,1));
		adminService.saveAdmin(admin, true);
		admin.setPassword("admin");
		admin.setUsername("admin");

		// Promo
		Promo promo = new Promo();
		promo.setName("ING2");
		promo.setEmail("ing2@cy-tech.fr");
		promoService.save(promo);

		Promo promo2 = new Promo();
		promo2.setName("ING1");
		promo2.setEmail("ing1@cy-tech.fr");
		promoService.save(promo2);

		// Pathway
		Pathway pathway = new Pathway();
		pathway.setName("GSI");
		pathway.setEmail("gsi@cy-tech.fr");
		pathwayService.save(pathway);

		Pathway pathway2 = new Pathway();
		pathway2.setName("GI");
		pathway2.setEmail("gi@cy-tech.fr");
		pathwayService.save(pathway2);

		// Address2
		Address address2 = new Address();
		address2.setNumber("1");
		address2.setStreet("rue Lebon");
		address2.setCity("Cergy");
		address2.setPostalCode(95000);
		address2.setCountry("France");
		addressService.addAddress(address2);

		// Subject
		Subject subject = new Subject();
		subject.setName("info");
		subjectService.save(subject);

		Subject subject2 = new Subject();
		subject2.setName("IA");
		subjectService.save(subject2);

		Subject subject3 = new Subject();
		subject3.setName("Math");
		subjectService.save(subject3);

		List<Subject> subjects = new ArrayList<>();
		subjects.add(subject);
		subjects.add(subject2);

		// Professor
		Professor professor = new Professor();
		professor.setAddress(address2);
		professor.setEmail("alex.smith@cy-tech.fr");
		professor.setPassword("prof");
		professor.setUsername("prof");
		professor.setFirstName("Alex");
		professor.setLastName("Smith");
		professor.setBirthday(LocalDate.of(2000,1,1));
		professor.setTeachingSubjects(subjects);
		professorService.saveProfessor(professor, true);
		professor.setPassword("prof");
		professor.setUsername("prof");


		// Address3
		Address address3 = new Address();
		address3.setNumber("1");
		address3.setStreet("rue Lebon");
		address3.setCity("Cergy");
		address3.setPostalCode(95000);
		address3.setCountry("France");
		addressService.addAddress(address3);

		// ClassCategory
		ClassCategory classCategory = new ClassCategory();
		classCategory.setName("TD");
		classCategory.setColor("#4a4aff");
		classCategoryService.save(classCategory);

		ClassCategory classCategory2 = new ClassCategory();
		classCategory2.setName("CM");
		classCategory2.setColor("#ff0000");
		classCategoryService.save(classCategory2);

		// Classe
		classeService.createClasse("ING2 GSI2", promo.getId().toString(), pathway.getId().toString(), "ing2-gsi2@cy-tech.fr");
		Classe classe = classeService.getListOfClasses().getFirst();

		List<StudentGroup> studentGroups = new ArrayList<>();
		studentGroups.add(pathway);
		studentGroups.add(promo);
		studentGroups.add(classe);

		// Course
		Course course = new Course();
		course.setStudentGroups(studentGroups);
		course.setSubject(subject);
		course.setProfessor(professor);
		course.setClassroom("A656");
		courseService.saveCourse(course);

		Course course2 = new Course();
		course2.setStudentGroups(studentGroups);
		course2.setSubject(subject2);
		course2.setProfessor(professor);
		course2.setClassroom("A664");
		courseService.saveCourse(course2);

		classeService.createClasse("ING2 GSI1", promo.getId().toString(), pathway.getId().toString(), "ing2-gsi1@cy-tech.fr");

		//Student
		Student student = new Student();
		student.setAddress(address3);
		student.setLastName("Johnson");
		student.setFirstName("Emma");
		student.setEmail("emma.johnson@cy-tech.fr");
		student.setBirthday(LocalDate.of(2000,1,1));
		student.setClasse(classe);
		studentService.saveStudent(student, true, null);
		student.setUsername("student");
		student.setPassword("student");

		LocalDate date = LocalDate.now();
		LocalDate monday;
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			monday = date.with(DayOfWeek.MONDAY).plusWeeks(1);
		} else {
			monday = date.with(DayOfWeek.MONDAY);
		}

		// CourseOccurrence
		CourseOccurrence courseOccurrence1 = new CourseOccurrence();
		courseOccurrence1.setCourse(course);
		courseOccurrence1.setClassroom("A688");
		courseOccurrence1.setProfessor(professor);
		courseOccurrence1.setDay(monday);
		courseOccurrence1.setBeginning(LocalTime.of(8, 0));
		courseOccurrence1.setEnd(LocalTime.of(11, 0));
		courseOccurrence1.setCategory(classCategory);
		courseOccurrenceService.saveCourseOccurrence(courseOccurrence1);

		CourseOccurrence courseOccurrence = new CourseOccurrence();
		courseOccurrence.setCourse(course);
		courseOccurrence.setDay(monday.plusDays(3));
		courseOccurrence.setBeginning(LocalTime.of(14, 45));
		courseOccurrence.setEnd(LocalTime.of(16, 15));
		courseOccurrence.setCategory(classCategory2);
		courseOccurrenceService.saveCourseOccurrence(courseOccurrence);

		// Grade
		Grade grade = new Grade();
		grade.setStudent(student);
		grade.setCourse(course);
		grade.setContext("Final exam DP");
		grade.setComment("GG");
		grade.setResult(20);
		grade.setSession(1);
		gradesService.createGrade(grade);

		Grade grade2 = new Grade();
		grade2.setStudent(student);
		grade2.setCourse(course2);
		grade2.setContext("Final exam");
		grade2.setComment("Good");
		grade2.setResult(15);
		grade2.setSession(1);
		gradesService.createGrade(grade2);
	}
}
