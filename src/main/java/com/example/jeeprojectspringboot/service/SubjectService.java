package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.SubjectRepository;
import com.example.jeeprojectspringboot.schoolmanager.Subject;
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
public class SubjectService {

	private final SubjectRepository subjectRepository;

	@Autowired
	public SubjectService(SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}

	public Subject getSubject(Long id) {
		return subjectRepository.findById(id).orElse(null);
	}

	public List<Subject> getAllSubjects() {
		return subjectRepository.findAll();
	}

	@Transactional
	public Subject save(Subject subject) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Subject>> errors = validator.validate(subject);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
		return subjectRepository.save(subject);
	}

	@Transactional
	public void deleteSubject(Long id) {
		subjectRepository.deleteById(id);
	}


}
