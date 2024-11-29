package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository  extends JpaRepository<Subject, Long> {
    Subject findById(long id);

    List<Subject> findAll();

    Subject save(Subject subject);

    void deleteById(long id);
}
