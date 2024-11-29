package com.example.jeeprojectspringboot.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataCleanupService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void deleteAllEntities() {
		// Désactiver les vérifications de clé étrangère
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

		// Exécuter les suppressions des données dans chaque table
		entityManager.createQuery("DELETE FROM Address").executeUpdate();
		entityManager.createQuery("DELETE FROM Admin").executeUpdate();
		entityManager.createQuery("DELETE FROM ClassCategory").executeUpdate();
		entityManager.createQuery("DELETE FROM Classe").executeUpdate();
		entityManager.createQuery("DELETE FROM Course").executeUpdate();
		entityManager.createQuery("DELETE FROM CourseOccurrence").executeUpdate();
		entityManager.createQuery("DELETE FROM Grade").executeUpdate();
		entityManager.createQuery("DELETE FROM Pathway").executeUpdate();
		entityManager.createQuery("DELETE FROM Professor").executeUpdate();
		entityManager.createQuery("DELETE FROM Promo").executeUpdate();
		entityManager.createQuery("DELETE FROM Student").executeUpdate();
		entityManager.createQuery("DELETE FROM Subject").executeUpdate();

		// Réactiver les vérifications de clé étrangère
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
	}
}
