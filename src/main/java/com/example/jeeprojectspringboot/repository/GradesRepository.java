package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Grade;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradesRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByStudent(Student student);

    void deleteByStudent(Student student);
}
