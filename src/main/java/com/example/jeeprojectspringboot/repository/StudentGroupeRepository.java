package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentGroupeRepository extends JpaAttributeConverter<StudentGroup,Long> {
    List<StudentGroup> findAll(StudentGroup studentGroup);

    StudentGroup findById(long id);

}
