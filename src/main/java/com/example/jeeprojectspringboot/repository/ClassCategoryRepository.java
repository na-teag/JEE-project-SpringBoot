package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.ClassCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassCategoryRepository  extends JpaRepository<ClassCategory, Long> {
    ClassCategory findById(long id);

    List<ClassCategory> findAll();

    ClassCategory save(ClassCategory classCategory);

    void deleteById(long id);
}
