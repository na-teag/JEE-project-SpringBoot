package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

	@Autowired
	private PersonService personService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ProfessorService professorService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private ClasseService classeService;
	@Autowired
	private SubjectService subjectService;

	@Autowired
	private HttpServletRequest request;


	@GetMapping("/users")
	public String getUsers(Model model, HttpSession session) {
		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			try{
				model.addAttribute("studentUsers", studentService.getAllStudents());
				model.addAttribute("adminUsers", adminService.getAllAdmins());
				model.addAttribute("profUsers", professorService.getAllProfessors());
				//model.addAttribute("classes", classeService.); TODO ajouter getAllClasses() quand ClasseService existera
				//model.addAttribute("subjects", subjectService.); TODO ajouter getAllSubject() quand SubjectService existera
				return "users";
			} catch (Exception e){
				e.printStackTrace();
				model.addAttribute("errorMessage", e.getMessage());
				return "users";
			}
		}
		return "login";
	}

	@GetMapping("/user")
	public String editUser(Model model, HttpSession session, @RequestParam("action") String action, @RequestParam("lastName") String lastName, @RequestParam("firstName") String firstName, @RequestParam("email") String email, @RequestParam(value = "id", required = false) Long id, @RequestParam("number") String number, @RequestParam("street") String street, @RequestParam("city") String city, @RequestParam("country") String country, @RequestParam("postalCode") int postalCode, @RequestParam(value = "classeId", required = false) Long classeId, @RequestParam(value= "subjectIdsString", required = false) List<String> subjectIdsString, @RequestParam(value = "birthday", required = false) String birthdayStr) {
		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			try {
				if ("saveStudent".equals(action)) {
					// if the request is about the creation/modification of a student
					if (classeId == null) {
						model.addAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
						return "users";
					}
					Address address = new Address();
					address.setStreet(street);
					address.setCity(city);
					address.setCountry(country);
					address.setPostalCode(postalCode);
					address.setNumber(number);

					Student student = new Student();
					student.setFirstName(firstName);
					student.setLastName(lastName);
					student.setEmail(email);
					student.setAddress(address);
					// student.setClasse(classeService.getClasseById(classeId); TODO add when classService is created
					if (id == null) {
						// if there is no id, then it is a new object
						LocalDate birthday = personService.getBirthday(birthdayStr);
						student.setBirthday(birthday);
						studentService.saveStudent(student, true, null);
					} else {
						// if there is an id the object already exists
						Classe formerClasse = studentService.findStudentById(id).getClasse();
						studentService.saveStudent(student, false, formerClasse.getId());
					}
					return "users";
				} else if ("saveProf".equals(action)) {
					// if the request is about the creation/modification of a professor
					if (subjectIdsString == null || subjectIdsString.isEmpty()) {
						model.addAttribute("errorMessage", "Paramètres manquants, impossible de traiter la requête");
						return "users";
					}

					// check that the Subjects exists
					List<Subject> subjects = new ArrayList<>();
					Subject subject;
					if (!subjectIdsString.contains("aucun")){
						for (String studentId : subjectIdsString) {
							subject = null; //subjectService.getSubjectById(studentId); TODO
							if (subject == null) {
								model.addAttribute("errorMessage", "Le sujet n'existe pas");
								return "users";
							}
							subjects.add(subject);
						}
					}
					Address address = new Address();
					address.setStreet(street);
					address.setCity(city);
					address.setCountry(country);
					address.setPostalCode(postalCode);
					address.setNumber(number);

					Professor professor = new Professor();
					professor.setFirstName(firstName);
					professor.setLastName(lastName);
					professor.setEmail(email);
					professor.setAddress(address);
					professor.setTeachingSubjects(subjects);
					if (id == null) {
						// if there is no id, then it is a new object
						LocalDate birthday = personService.getBirthday(birthdayStr);
						professor.setBirthday(birthday);
						professorService.saveProfessor(professor, true);
					} else {
						// if there is an id the object already exists
						professorService.saveProfessor(professor, false);
					}
					return "users";
				} else if ("saveAdmin".equals(action)) {
					// if the request is about the creation/modification of an administrator

					Address address = new Address();
					address.setStreet(street);
					address.setCity(city);
					address.setCountry(country);
					address.setPostalCode(postalCode);
					address.setNumber(number);

					Admin admin = new Admin();
					admin.setFirstName(firstName);
					admin.setLastName(lastName);
					admin.setEmail(email);
					admin.setAddress(address);
					if (id == null) {
						// if there is no id, then it is a new object
						LocalDate birthday = personService.getBirthday(birthdayStr);
						admin.setBirthday(birthday);
						adminService.saveAdmin(admin, true);
					} else {
						// if there is an id the object already exists
						adminService.saveAdmin(admin, false);
					}
					return "users";
				} else if ("delete".equals(action) && id != null) {
					// if the request is about the deletion of a user, no matter the type
					Person user = personService.getUserById(id);
					if (user instanceof Student) {
						studentService.deleteStudentById(id);
					} else if (user instanceof Professor) {
						professorService.deleteProfessorById(id);
					} else if (user instanceof Admin) {
						adminService.deleteAdminById(id);
						// si l'admin se supprime lui même, il faut le déconnecter
						if (user.getId().equals(id)) {
							return "redirect:/logout";
						}
					}
					return "users";
				} else {
					model.addAttribute("errorMessage", "Requête non reconnue");
					return "users";
				}
			} catch (Exception e){
				e.printStackTrace();
				model.addAttribute("errorMessage", e.getMessage());
				return "users";
			}
		}
		return "error";
	}

}