package com.example.jeeprojectspringboot.schoolmanager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public abstract class StudentGroup extends Emailable {

	@Column(name = "name")
	@NotBlank(message = "Le nom ne peut pas être vide")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
