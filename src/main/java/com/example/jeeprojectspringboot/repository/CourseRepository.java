package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Trouver les cours par la classe d'un étudiant
    List<Course> findByStudentGroupContaining(Classe classe);

    // Trouver les cours par la promo d'un étudiant
    List<Course> findByStudentGroupContaining(Promo promo);

    // Trouver les cours par le pathway d'un étudiant
    List<Course> findByStudentGroupContaining(Pathway pathway);

    List<Course> findByProfessor(Professor professor);
}
