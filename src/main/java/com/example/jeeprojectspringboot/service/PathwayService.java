package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.PathwayRepository;
import com.example.jeeprojectspringboot.schoolmanager.Pathway;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PathwayService {

	private final PathwayRepository pathwayRepository;
	private SessionFactory sessionFactory;

	@Autowired
	public PathwayService(PathwayRepository pathwayRepository) {
		this.pathwayRepository = pathwayRepository;
	}

	public Pathway getPathway(Long id) {
		return pathwayRepository.findById(id).orElse(null);
	}

	public List<Pathway> getAllPathways() {
		return pathwayRepository.findAll();
	}

	public Pathway save(Pathway pathway) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Pathway>> errors = validator.validate(pathway);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
		return pathwayRepository.save(pathway);
	}

	public void deletePathway(Long id) {
		pathwayRepository.deleteById(id);
	}
}
