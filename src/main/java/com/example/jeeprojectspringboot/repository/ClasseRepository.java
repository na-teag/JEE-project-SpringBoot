package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Classe;
import com.example.jeeprojectspringboot.schoolmanager.Pathway;
import com.example.jeeprojectspringboot.schoolmanager.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Classe findById(long id);
    List<Classe> findAll();

    List<Classe> findByPathway(Pathway pathway);

    List<Classe> findByPromo(Promo promo);


    void deleteById(Long id);
}
