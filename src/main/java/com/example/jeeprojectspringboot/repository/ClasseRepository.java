package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Classe;
import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Classe findById(long id);
    List<Classe> findAll();

    @Query("SELECT c FROM Classe c WHERE c.promo = :studentGroup OR c.pathway = :studentGroup")
    List<Classe> findByPathwayOrPromo(@Param("studentGroup") StudentGroup studentGroup);


    void deleteById(Long id);
}
