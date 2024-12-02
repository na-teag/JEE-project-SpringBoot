package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.CourseOccurrenceRepository;
import com.example.jeeprojectspringboot.schoolmanager.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class CourseOccurrenceService {

	private final CourseOccurrenceRepository courseOccurrenceRepository;
	private final PersonService personService;
	private final CourseService courseService;

	@Autowired
	@Lazy
	public CourseOccurrenceService(CourseOccurrenceRepository courseOccurrenceRepository, PersonService personService, CourseService courseService) {
		this.courseOccurrenceRepository = courseOccurrenceRepository;
		this.personService = personService;
		this.courseService = courseService;
	}

	public void deleteByProfessor(Professor professor){
		courseOccurrenceRepository.deleteByProfessor(professor);
	}

	public CourseOccurrence getCourseOccurrenceById(long id){
		return courseOccurrenceRepository.findById(id);
	}

	public CourseOccurrence saveCourseOccurrence(CourseOccurrence courseOccurrence) { return courseOccurrenceRepository.save(courseOccurrence); }

	public void deleteCourseOccurrence(Long id){
		courseOccurrenceRepository.deleteById(id);
	}

	public List<CourseOccurrence> findByCourse(Course course) {
		if (course == null || course.getId() == null) {
			throw new IllegalArgumentException("Course must not be null and must have a valid ID.");
		}
		return courseOccurrenceRepository.findByCourse(course);
	}

	public List<CourseOccurrence> getAllCourseOccurrence(){return courseOccurrenceRepository.findAll();}

	private static Map<String, String> getCourseDetails(CourseOccurrence courseOccurrence) {
		Map<String, String> courseDetails = new HashMap<>();
		courseDetails.put("title", courseOccurrence.getCourse().getSubject().getName());
		courseDetails.put("startTime", String.format("%02d", courseOccurrence.getBeginning().getHour()) + "h" + String.format("%02d", courseOccurrence.getBeginning().getMinute()));
		courseDetails.put("endTime", String.format("%02d", courseOccurrence.getEnd().getHour()) + "h" + String.format("%02d", courseOccurrence.getEnd().getMinute()));
		courseDetails.put("room", courseOccurrence.getClassroom());
		courseDetails.put("professor", courseOccurrence.getProfessor().getFirstName() + " " + courseOccurrence.getProfessor().getLastName());
		courseDetails.put("type", courseOccurrence.getCategory().getName());
		courseDetails.put("color", courseOccurrence.getCategory().getColor());

		String studentGroupsNames = "";
		for (StudentGroup studentGroup : courseOccurrence.getCourse().getStudentGroups()) {
			if (!studentGroupsNames.isEmpty()){
				studentGroupsNames += ", ";
			}
			studentGroupsNames += studentGroup.getName();
		}
		courseDetails.put("classes", studentGroupsNames);
		return courseDetails;
	}

	private Map<String, List<Map<String, String>>> getScheduleForCoursesAndDays(List<LocalDate> days, Map<String, List<Map<String, String>>> schedule, List<Course> courses) {
		for (LocalDate day: days) {
			List<CourseOccurrence> coursesOfDayInput = courseOccurrenceRepository.findCourseOccurrencesForCoursesAndDate(courses, day);
			List<Map<String, String>> coursesOfDayOutput = new ArrayList<>();
			for (CourseOccurrence courseOccurrence : coursesOfDayInput) {
				Map<String, String> courseDetails = getCourseDetails(courseOccurrence);
				coursesOfDayOutput.add(courseDetails);
			}
			schedule.put(day.getDayOfWeek().toString(), coursesOfDayOutput);
		}
		return schedule;
	}

	public Map<String, List<Map<String, String>>> getCoursesByPersonNumberAndDays(String personNumber, List<LocalDate> days) {
		Person user = personService.getUserByPersonNumber(personNumber);
		Map<String, List<Map<String, String>>> schedule = new HashMap<>();
		if (Admin.class.getName().equals(user.getClass().getName())) {
			for (LocalDate day : days) {
				schedule.put(day.getDayOfWeek().toString(), new ArrayList<>());
			}
		} else if (Professor.class.getName().equals(user.getClass().getName())) {
			Professor professor = (Professor) user;
			List<Course> courses = courseService.getCoursesOfProfessor(professor);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		} else {
			// student case
			Student student = (Student) user;
			List<Course> courses = courseService.getCoursesOfStudent(student);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		}
		return schedule;
	}

	public void validateSchedule(LocalDate day, LocalTime beginning, LocalTime end, Model model) {
		if (day.getDayOfWeek().getValue() == 6 || day.getDayOfWeek().getValue() == 7) {
			model.addAttribute("errorMessage", "Les cours ne peuvent pas être planifiés un samedi ou un dimanche.");

			//throw new IllegalArgumentException("Les cours ne peuvent pas être planifiés un samedi ou un dimanche.");
		}
		if (beginning.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(20, 0))) {
			throw new IllegalArgumentException("Les cours doivent se dérouler entre 08h00 et 20h00.");
		}
		if (beginning.getMinute() % 15 != 0 || end.getMinute() % 15 != 0) {
			throw new IllegalArgumentException("Les horaires doivent finir en 00, 15, 30 ou 45 minutes.");
		}
		if (!beginning.isBefore(end)) {
			throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
		}
	}

	@Transactional
	public CourseOccurrence save(CourseOccurrence courseOccurrence) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<CourseOccurrence>> errors = validator.validate(courseOccurrence);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
		return courseOccurrenceRepository.save(courseOccurrence);
	}
}
