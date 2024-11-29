package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Grade;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepository extends JpaRepository<Grade, Long> {

    Grade findById(long id);
    List<Grade> findByStudent(Student student);

    void deleteByStudent(Student student);
    Grade findByStudentAndCourse(Student student, Course course);
    List<Grade> findByCourse(Course course);

    void deleteById(long id);
}
