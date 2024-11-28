package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.GradesRepository;
import com.example.jeeprojectspringboot.schoolmanager.Grade;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradesService {

    @Autowired
    private GradesRepository gradesRepository;

    public List<Grade> getGradesForStudent(Student student) {
        return gradesRepository.findByStudent(student);
    }

    // TODO ajouter la gestion des mails quand on assigne une nouvelle note

    @Transactional
    public void deleteGradesForStudent(Student student) {
        gradesRepository.deleteByStudent(student);
    }
}
