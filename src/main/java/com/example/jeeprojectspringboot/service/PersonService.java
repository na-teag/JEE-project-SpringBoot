package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.PersonRepository;
import com.example.jeeprojectspringboot.schoolmanager.Person;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
}
