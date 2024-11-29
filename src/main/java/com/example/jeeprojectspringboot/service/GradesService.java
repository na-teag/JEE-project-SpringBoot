package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.GradesRepository;
import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Grade;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradesService {

    @Autowired
    private GradesRepository gradesRepository;

    public List<Grade> getGradesForStudent(Student student) {
        return gradesRepository.findByStudent(student);
    }

    public void deleteById(Long id){gradesRepository.deleteById(id);}

    public List<Grade> findByCourse(Course course) {
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException("Course must not be null and must have a valid ID.");
        }
        return gradesRepository.findByCourse(course);
    }
}
