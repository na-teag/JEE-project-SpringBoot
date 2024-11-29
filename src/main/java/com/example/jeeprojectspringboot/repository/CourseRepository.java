package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Classe;
import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Pathway;
import com.example.jeeprojectspringboot.schoolmanager.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStudentGroupsContaining(Classe classe);

    List<Course> findByStudentGroupsContaining(Promo promo);

    List<Course> findByStudentGroupsContaining(Pathway pathway);

    Course save(Course course);

    void deleteById(long id);
}