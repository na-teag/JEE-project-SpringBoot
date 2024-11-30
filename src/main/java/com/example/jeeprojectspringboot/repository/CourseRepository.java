package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findById(long id);

    List<Course> findBySubject(Subject subject);

    // Trouver les cours par la classe d'un étudiant

    List<Course> findByStudentGroupsContaining(Classe classe);

    List<Course> findByStudentGroupsContaining(Promo promo);

    List<Course> findByStudentGroupsContaining(Pathway pathway);

    List<Course> findByProfessor(Professor professor);

    Course save(Course course);

    void deleteById(long id);
}