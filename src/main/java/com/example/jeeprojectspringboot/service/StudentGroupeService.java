package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.StudentGroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentGroupeService{

    @Autowired
    private StudentGroupeRepository studentGroupeRepository;



    public void deleteById(Long id){studentGroupeRepository.deleteById(id);}
}
