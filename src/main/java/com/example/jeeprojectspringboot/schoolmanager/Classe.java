package com.example.jeeprojectspringboot.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "classe")
public class Classe extends StudentGroup {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la filière
	@JoinColumn(name = "pathway_id", nullable = false)
	@NotNull(message = "La flière ne peut pas être vide")
	private Pathway pathway;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la promo
	@JoinColumn(name = "promo_id", nullable = false)
	@NotNull(message = "La promotion ne peut pas être vide")
	private Promo promo;

	public Pathway getPathway() {
		return pathway;
	}
	public void setPathway(Pathway pathway) {
		this.pathway = pathway;
	}

	public Promo getPromo() {
		return promo;
	}
	public void setPromo(Promo promo) {
		this.promo = promo;
	}
}
