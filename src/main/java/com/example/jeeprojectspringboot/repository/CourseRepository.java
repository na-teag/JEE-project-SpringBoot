package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Trouver les cours par la classe d'un étudiant
    @Query("SELECT c FROM Course c JOIN c.studentGroups sg WHERE sg = :studentGroup")
    List<Course> findByStudentGroup(StudentGroup studentGroup);

    List<Course> findByProfessor(Professor professor);
}
