package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.ClasseRepository;
import com.example.jeeprojectspringboot.repository.PathwayRepository;
import com.example.jeeprojectspringboot.repository.PromoRepository;
import com.example.jeeprojectspringboot.schoolmanager.Classe;
import com.example.jeeprojectspringboot.schoolmanager.Pathway;
import com.example.jeeprojectspringboot.schoolmanager.Promo;
import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ClasseService {

	@Autowired
	private ClasseRepository classeRepository;

	@Autowired
	private PathwayRepository pathwayRepository;

	@Autowired
	private PromoRepository promoRepository;

	public Classe getClasse(Long id) {
		return classeRepository.findById(id).orElse(null);
	}
	@Transactional
	public List<Classe> getListOfClasses() {
		try {
			return classeRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public List<Classe> getClassesByStudentGroup(StudentGroup studentGroup) {
		if (Classe.class.getName().equals(studentGroup.getClass().getName())) {
			Classe classe = (Classe) studentGroup;
			return List.of(classe);
		}

		try {
			return classeRepository.findByPathwayOrPromo(studentGroup, studentGroup);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public String createClasse(String classeName, String promoId, String pathwayId, String email) {
		Pathway pathway = pathwayRepository.findById(Long.parseLong(pathwayId));
		Promo promo = promoRepository.findById(Long.parseLong(promoId));
		if (pathway == null || promo == null) {
			return "Pathway or Promo not found";
		}
		Classe classe = new Classe();
		classe.setName(classeName);
		classe.setEmail(email);
		classe.setPathway(pathway);
		classe.setPromo(promo);

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Classe>> errors = validator.validate(classe);
		if (errors.isEmpty()) {
			classeRepository.save(classe);
			return null;
		}
		String errorString = "";
		for (ConstraintViolation<Classe> error : errors) {
			errorString += error.getMessage() + "\n";
		}
		return errorString;
	}


	@Transactional
	public String updateClasseById(String id, String classeName, String promoId, String pathwayId, String email) {
		Classe classe = classeRepository.findById(Long.parseLong(id));
		if (classe == null) {
			return "Classe not found with ID: " + id;
		}

		Pathway pathway = pathwayRepository.findById(Long.parseLong(pathwayId));
		Promo promo = promoRepository.findById(Long.parseLong(promoId));

		if (pathway == null || promo == null) {
			return "Pathway or Promo not found";
		}
		classe.setName(classeName);
		classe.setEmail(email);
		classe.setPathway(pathway);
		classe.setPromo(promo);

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Classe>> errors = validator.validate(classe);
		if (errors.isEmpty()) {
			classeRepository.save(classe);
			return null;
		}

		String errorString = "";
		for (ConstraintViolation<Classe> error : errors) {
			errorString += error.getMessage() + "\n";
		}
		return errorString;
	}

	@Transactional
	public String deleteClasseById(String id) {
		try {
			if (!classeRepository.existsById(Long.valueOf(id))) {
				return "Classe not found with ID: " + id;
			}
			classeRepository.deleteById(Long.valueOf(id));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
