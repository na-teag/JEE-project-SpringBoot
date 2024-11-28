package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.PersonRepository;
import com.example.jeeprojectspringboot.schoolmanager.Person;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private SessionFactory sessionFactory;
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
}
