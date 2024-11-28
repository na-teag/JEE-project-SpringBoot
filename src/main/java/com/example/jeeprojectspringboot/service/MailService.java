package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.*;

@Service
public class MailService {

	private final StudentService studentService;

	@Autowired
	public MailService(StudentService studentService) {
		this.studentService = studentService;
	}

	/*
	 * se connecter à https://app.debugmail.io/app/login
	 *  username : jeeproject.2024@gmail.com
	 *  password : MyP@ssw0rd123
	 */

	private static void sendEmailToDebugMail(String senderEmail, Person person, String object, String body) {
		String host = "app.debugmail.io";
		final String username = "f968c50b-007a-4f46-b20e-554bdd817458";
		final String password = "f914de2c-05b6-4797-88cd-6dd108f6e1ba";
		int port = 9025;

		// Configuration des propriétés SMTP
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		// Création de la session
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Création du message
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(person.getEmail()));
			message.setSubject(object);
			message.setText(body);

			// Envoi du message
			Transport.send(message);
			System.out.println("Mail envoyé avec succès !");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}



	private void sendEmailToPerson(String senderEmail, Person person, String object, String body){
		sendEmailToDebugMail(senderEmail, person, object, body);
		System.out.println(senderEmail + " to " + person.getEmail() + " : " + object + "\n " + body);
	}

	private void sendEmailToGroup(String senderEmail, StudentGroup studentGroup, String object, String body) {
		// TODO ajouter le findClasseByStudentGroup() quand ClasseService
		List<Classe> classeList = new ArrayList<>();//classeManager.getClassesByStudentGroup(studentGroup);
		List<Student> studentList = new ArrayList<>();
		for (Classe classe : classeList) {
			studentList.addAll(studentService.findStudentByClasseId(classe.getId()));
		}
		Set<Long> seenIds = new HashSet<>();
		List<Student> uniqueStudents = new ArrayList<>();

		// delete the potential duplicates
		for (Student student : studentList) {
			if (seenIds.add(student.getId())) {
				uniqueStudents.add(student);
			}
		}
		for (Student student : uniqueStudents){
			sendEmailToPerson(senderEmail, student, object, body);
		}
	}


	public void sendEmail(String senderEmail, Emailable emailable, String object, String body){
		if (emailable.getClass().getName().equals(Student.class.getName()) || emailable.getClass().getName().equals(Professor.class.getName()) || emailable.getClass().getName().equals(Admin.class.getName())){
			sendEmailToPerson(senderEmail, (Person)emailable, object, body);
		} else if (emailable.getClass().getName().equals(Promo.class.getName()) || emailable.getClass().getName().equals(Pathway.class.getName()) || emailable.getClass().getName().equals(Classe.class.getName())) {
			sendEmailToGroup(senderEmail, (StudentGroup) emailable, object, body);
		}
	}

	public void sendEmail(String senderEmail, List<Emailable> emailableList, String object, String body) {
		for (Emailable emailable : emailableList){
			sendEmail(senderEmail, emailable, object, body);
		}
	}
}
