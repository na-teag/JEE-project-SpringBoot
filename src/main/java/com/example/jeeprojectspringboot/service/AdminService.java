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
			String password = String.format("%02d%02d%d", admin.getBirthday().getDayOfMonth(), admin.getBirthday().getMonthValue(), admin.getBirthday().getYear());
			admin.setPassword(password);
		}

		// set the username based on the name
		String username = "e-" + admin.getFirstName().charAt(0);
		username = username.toLowerCase();
		String lastNameCut = admin.getLastName().length() > 7 ? admin.getLastName().substring(0, 7) : admin.getLastName();
		lastNameCut = lastNameCut.toLowerCase();
		if (personService.getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
			int x = 1;
			while (personService.getUserByUsername(username + lastNameCut + x) != null) {
				x++;
			}
			admin.setUsername(username + lastNameCut + x);
		} else {
			admin.setUsername(username + lastNameCut);
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

		return adminRepository.save(admin);
	}

	@Transactional
	public void deleteAdminById(Long id) {
		adminRepository.deleteById(id);
	}
}