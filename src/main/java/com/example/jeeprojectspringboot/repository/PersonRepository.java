package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Requête pour obtenir le plus grand numéro de personne (personNumber)
    @Query("SELECT MAX(p.personNumber) FROM Person p")
    String findMaxPersonNumber();

    Person findByUsername(String username);
}
