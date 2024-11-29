package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.CourseOccurence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseOccurrenceRepository extends JpaRepository<CourseOccurence, Long> {
	List<CourseOccurence> findAll();

	@Query("SELECT co FROM CourseOccurence co WHERE co.course IN :courses AND co.day = :date")
	List<CourseOccurence> findCourseOccurrencesForCoursesAndDate(@Param("courses") List<Course> courses, @Param("date") LocalDate date);

	void deleteById(long id);


}
