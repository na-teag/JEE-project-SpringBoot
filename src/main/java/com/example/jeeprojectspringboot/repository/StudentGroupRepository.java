package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    StudentGroup save(StudentGroup studentGroup);

    StudentGroup findById(long id);

    void deleteById(Long id);

    
}
