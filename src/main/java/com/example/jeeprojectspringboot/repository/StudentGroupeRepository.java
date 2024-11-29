package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentGroupeRepository extends JpaRepository<StudentGroup,Long> {
    List<StudentGroup> findAllById(long id);

    StudentGroup findById(long id);

    void deleteById(Long id);


}
