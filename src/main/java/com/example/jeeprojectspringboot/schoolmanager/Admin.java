package com.example.jeeprojectspringboot.schoolmanager;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends Person {}