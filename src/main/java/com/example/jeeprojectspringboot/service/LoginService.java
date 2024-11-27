package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.schoolmanager.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final PersonService personService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(PersonService personService) {
        this.personService = personService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Méthode pour authentifier un utilisateur
    public Person authenticate(String username, String password) throws IllegalAccessException {
        Person person = personService.getUserByUsername(username);
        if (person != null && passwordEncoder.matches(password, person.getPasswordHash())) {
            return person;
        }
        throw new IllegalAccessException("Invalid username or password");
    }
}