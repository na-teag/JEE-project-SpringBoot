package com.example.jeeprojectspringboot.repository;

import com.example.jeeprojectspringboot.schoolmanager.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Student findById(long id);

	List<Student> findAll();

	Student save(Student student);

	@Query("SELECT s FROM Student s WHERE s.classe.id = :classeId")
	List<Student> findByClasseId(@Param("classeId") long classeId);

	@Query("SELECT s FROM Student s WHERE s.classe.pathway.id = :pathwayId")
	List<Student> findStudentsByPathway(@Param("pathwayId") long pathwayId);

	@Query("SELECT s FROM Student s WHERE s.classe.promo.id = :promoId")
	List<Student> findStudentsByPromo(@Param("promoId") long promoId);
}
