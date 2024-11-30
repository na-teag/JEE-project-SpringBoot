package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.ClassCategoryRepository;
import com.example.jeeprojectspringboot.schoolmanager.ClassCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ClassCategoryService {

    private final ClassCategoryRepository classCategoryRepository;

    @Autowired
    public ClassCategoryService(ClassCategoryRepository classCategoryRepository) {
        this.classCategoryRepository = classCategoryRepository;
    }

    public ClassCategory getClassCategory(Long id) {
        return classCategoryRepository.findById(id).orElse(null);
    }

    public List<ClassCategory> getAllClassCategories() {
        return classCategoryRepository.findAll();
    }

    @Transactional
    public ClassCategory save(ClassCategory classCategory) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ClassCategory>> errors = validator.validate(classCategory);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
        return classCategoryRepository.save(classCategory);
    }


    @Transactional
    public void deleteClassCategory(Long id) {
        classCategoryRepository.deleteById(id);
    }
}

