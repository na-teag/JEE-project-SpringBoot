package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.StudentGroupRepository;
import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentGroupService {

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    public StudentGroup save(StudentGroup studentGroup) { return this.studentGroupRepository.save(studentGroup); }

    public StudentGroup getStudentGroupFromId(long id) {
        return studentGroupRepository.findById(id);
    }

    public void deleteById(Long id){studentGroupRepository.deleteById(id);}

}