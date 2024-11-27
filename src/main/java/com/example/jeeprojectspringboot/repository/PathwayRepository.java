package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Pathway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathwayRepository  extends JpaRepository<Pathway, Long> {
	Pathway findById(long id);
	List<Pathway> findAll();
	Pathway save(Pathway pathway);
	void deleteById(long id);
}
