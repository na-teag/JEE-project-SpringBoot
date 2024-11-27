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

    // Trouver les cours par la classe d'un étudiant
    List<Course> findByStudentGroupsContaining(Classe classe);

    // Trouver les cours par la promo d'un étudiant
    List<Course> findByStudentGroupsContaining(Promo promo);

    // Trouver les cours par le pathway d'un étudiant
    List<Course> findByStudentGroupsContaining(Pathway pathway);
}
