package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Classe;
import com.example.jeeprojectspringboot.schoolmanager.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    Classe findById(long id);
    List<Classe> findAll();
    List<Classe> findByPathwayOrPromo(StudentGroup studentGroup1, StudentGroup studentGroup2);
    void deleteById(Long id);
}
