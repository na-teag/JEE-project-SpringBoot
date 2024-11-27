package com.example.jeeprojectspringboot.schoolmanager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "subject")
public class Subject extends Model {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "le nom ne peut pas être vide")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
