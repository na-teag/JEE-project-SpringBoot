package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.CourseOccurence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseOccurenceRepository extends JpaRepository<CourseOccurence,Long>{
    List<CourseOccurence> findAll();

    CourseOccurence findById(long id);

    void deleteById(long id);

    List<CourseOccurence> findByCourse(Course course);


}
