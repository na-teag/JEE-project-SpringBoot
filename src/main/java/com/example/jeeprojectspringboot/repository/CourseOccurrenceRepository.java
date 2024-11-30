package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.CourseOccurrence;
import com.example.jeeprojectspringboot.schoolmanager.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseOccurrenceRepository extends JpaRepository<CourseOccurrence, Long> {
	List<CourseOccurrence> findAll();

	CourseOccurrence findById(long id);

	void deleteById(long id);

	List<CourseOccurrence> findByCourse(Course course);

	@Query("SELECT co FROM CourseOccurrence co WHERE co.course IN :courses AND co.day = :date")
	List<CourseOccurrence> findCourseOccurrencesForCoursesAndDate(@Param("courses") List<Course> courses, @Param("date") LocalDate date);

	void deleteByProfessor(Professor professor);

}
