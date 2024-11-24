package com.example.jeeprojectspringboot.schoolmanager;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "promo")
public class Promo extends StudentGroup {}
