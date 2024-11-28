package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.ProfessorRepository;
import com.example.jeeprojectspringboot.schoolmanager.Pathway;
import com.example.jeeprojectspringboot.schoolmanager.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public Optional<Professor> getProfessorById(Long id) {
        return professorRepository.findById(id);
    }

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

}
