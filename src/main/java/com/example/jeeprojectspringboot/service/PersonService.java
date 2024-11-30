package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.PersonRepository;
import com.example.jeeprojectspringboot.schoolmanager.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MailService mailService;

    @Autowired
    public PersonService(PersonRepository personRepository, MailService mailService) {
        this.personRepository = personRepository;
        this.mailService = mailService;
    }


    public Person getUserByUsername(String username) {
        return personRepository.findByUsername(username); // Appel à la méthode générée par Spring Data JPA
    }

    public Person getUserById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public Person getUserByPersonNumber(String personNumber) {
        return personRepository.findByPersonNumber(personNumber);
    }

    public Person emailExists(String email) {
        return personRepository.findByEmail(email);
    }


    /*
     * There is no function to delete a Person, because of specific action that must be done when deleting a specific type of person
     * See deleteStudent and deleteProfessor
     */


    @Transactional
    public void setPersonNumber(Person person) {
        if (person.getPersonNumber() == null) {
            String maxPersonNumber = personRepository.findMaxPersonNumber();
            if (maxPersonNumber == null) {
                person.setPersonNumber("00000001");
            } else {
                int nextPersonNumber = Integer.parseInt(maxPersonNumber) + 1;
                person.setPersonNumber(String.format("%08d", nextPersonNumber));
            }
        }
    }

    public LocalDate getBirthday(String birthdayStr) throws IllegalArgumentException, DateTimeParseException  {
        if (birthdayStr == null || birthdayStr.isEmpty()) {
            throw new IllegalArgumentException("birthdayStr is null or empty");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(birthdayStr, formatter);
    }


    public void sendAccountCreationMail(Person person) {
        mailService.sendEmail("do.not.reply@cytech.fr", person, "Création de compte", "Bonjour " + person.getFirstName() + ",\nVous recevez cet email car un compte viens de vous être créé sur l'ENT de CYTech.\nVotre identifiant est le suivant : " + person.getUsername() + "\nVotre mot de passe est votre date de naissance au format ddMMyyyy\n\nVous retrouverez sur l'ENT toutes les informations nécéssaires pour répondre à vos besoins.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail");
    }

    public void setUsername(Person person) {
        // set the username based on the name
        String username = "e-" + person.getFirstName().charAt(0);
        username = username.toLowerCase();
        String lastNameCut = person.getLastName().length() > 7 ? person.getLastName().substring(0, 7) : person.getLastName();
        lastNameCut = lastNameCut.toLowerCase();
        if (getUserByUsername(username + lastNameCut) != null) { // if the username already exists, add the most little number behind
            int x = 1;
            while (getUserByUsername(username + lastNameCut + x) != null) {
                x++;
            }
            person.setUsername(username + lastNameCut + x);
        } else {
            person.setUsername(username + lastNameCut);
        }
    }

    public void setPassword(Person person) {
        String password = String.format("%02d%02d%d", person.getBirthday().getDayOfMonth(), person.getBirthday().getMonthValue(), person.getBirthday().getYear());
        person.setPassword(password);
    }

}
