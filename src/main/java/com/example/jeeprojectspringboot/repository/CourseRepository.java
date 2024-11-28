package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findById(long id);

    // Trouver les cours par la classe d'un étudiant
    List<Course> findByStudentGroupsContaining(Classe classe);

    // Trouver les cours par la promo d'un étudiant
    List<Course> findByStudentGroupsContaining(Promo promo);

    // Trouver les cours par le pathway d'un étudiant
    List<Course> findByStudentGroupsContaining(Pathway pathway);

    List<Course> findByProfessor(Professor professor);
}
