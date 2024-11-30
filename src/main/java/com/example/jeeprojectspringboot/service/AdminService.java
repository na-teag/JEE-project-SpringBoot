package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.AdminRepository;
import com.example.jeeprojectspringboot.schoolmanager.Admin;
import com.example.jeeprojectspringboot.schoolmanager.Person;
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
public class AdminService {

	private final AdminRepository adminRepository;
	private final PersonService personService;

	@Autowired
	public AdminService(AdminRepository adminRepository, PersonService personService) {
		this.adminRepository = adminRepository;
		this.personService = personService;
	}

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

	public Admin findAdminById(Long id) {
		return adminRepository.findById(id).orElse(null);
	}

	@Transactional
	public Admin saveAdmin(Admin admin, boolean isNew) {
		if (isNew) {
			personService.setPersonNumber(admin);
			personService.setPassword(admin);
			personService.setUsername(admin);
		}


		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Admin>> violations = validator.validate(admin);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Person otherUser = personService.emailExists(admin.getEmail());
		if(otherUser != null && !otherUser.getId().equals(admin.getId())) {
			throw new IllegalArgumentException("Cet email est déjà attribué");
		}

		if (isNew){
			personService.sendAccountCreationMail(admin);
		}

		return adminRepository.save(admin);
	}

	@Transactional
	public void deleteAdminById(Long id) {
		adminRepository.deleteById(id);
	}
}