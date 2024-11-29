package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.CourseOccurenceRepository;
import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseOccurenceService {

    @Autowired
    private CourseOccurenceRepository courseOccurenceRepository;
    public CourseOccurence getCourseOccurenceById(long id){
        return courseOccurenceRepository.findById(id);
    }

    public void deleteCourseOccurence(Long id){
        courseOccurenceRepository.deleteById(id);
    }

    public List<CourseOccurence> findByCourse(Course course) {
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException("Course must not be null and must have a valid ID.");
        }
        return courseOccurenceRepository.findByCourse(course);
    }
}
