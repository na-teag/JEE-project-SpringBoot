package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    StudentGroup findById(long id);

    
}
