package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.StudentGroupRepository;
import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentGroupService {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    public StudentGroup getStudentGroupFromId(long id) {
        return studentGroupRepository.findById(id);
    }

    public void deleteById(Long id){studentGroupRepository.deleteById(id);}

}