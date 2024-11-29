package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.CourseOccurrenceRepository;
import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseOccurrenceService {

	private final CourseOccurrenceRepository courseOccurrenceRepository;
	private final PersonService personService;
	private final CourseService courseService;

	@Autowired
	public CourseOccurrenceService(CourseOccurrenceRepository courseOccurrenceRepository, PersonService personService, CourseService courseService) {
		this.courseOccurrenceRepository = courseOccurrenceRepository;
		this.personService = personService;
		this.courseService = courseService;
	}


	/*
	 * TODO when CourseOccurrenceService is created :
	 *
	 * add a deleteByProfessor() method that delete every CourseOccurence of a given professor
	 * use it the TODO in ProfessorService line 77
	 */

	private static Map<String, String> getCourseDetails(CourseOccurence courseOccurence) {
		Map<String, String> courseDetails = new HashMap<>();
		courseDetails.put("title", courseOccurence.getCourse().getSubject().getName());
		courseDetails.put("startTime", String.format("%02d", courseOccurence.getBeginning().getHour()) + "h" + String.format("%02d", courseOccurence.getBeginning().getMinute()));
		courseDetails.put("endTime", String.format("%02d", courseOccurence.getEnd().getHour()) + "h" + String.format("%02d", courseOccurence.getEnd().getMinute()));
		courseDetails.put("room", courseOccurence.getClassroom());
		courseDetails.put("professor", courseOccurence.getProfessor().getFirstName() + " " + courseOccurence.getProfessor().getLastName());
		courseDetails.put("type", courseOccurence.getCategory().getName());
		courseDetails.put("color", courseOccurence.getCategory().getColor());

		String studentGroupsNames = "";
		for (StudentGroup studentGroup : courseOccurence.getCourse().getStudentGroups()) {
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
			List<CourseOccurence> coursesOfDayInput = courseOccurrenceRepository.findCourseOccurrencesForCoursesAndDate(courses, day);
			List<Map<String, String>> coursesOfDayOutput = new ArrayList<>();
			for (CourseOccurence courseOccurence : coursesOfDayInput) {
				Map<String, String> courseDetails = getCourseDetails(courseOccurence);
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
			List<Course> courses = courseService.findB(student);
			schedule = getScheduleForCoursesAndDays(days, schedule, courses);
		}
		return schedule;
	}
}
