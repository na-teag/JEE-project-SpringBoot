package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.CourseRepository;
import com.example.jeeprojectspringboot.schoolmanager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Méthode pour récupérer les cours d'un étudiant
    public List<Course> getCoursesOfStudent(Student student) {
        Classe studentClasse = student.getClasse();
        Promo studentPromo = studentClasse.getPromo();
        Pathway studentPathway = studentClasse.getPathway();

        List<Course> courses = new ArrayList<>();

        // Récupération des cours pour la classe de l'étudiant
        List<Course> tmp = courseRepository.findByStudentGroup(studentClasse);
        if (tmp != null) {
            courses.addAll(tmp);
        }

        // Récupération des cours pour la promo de l'étudiant
        tmp = courseRepository.findByStudentGroup(studentPromo);
        if (tmp != null) {
            courses.addAll(tmp);
        }

        // Récupération des cours pour le pathway de l'étudiant
        tmp = courseRepository.findByStudentGroup(studentPathway);
        if (tmp != null) {
            courses.addAll(tmp);
        }

        // Suppression des doublons en utilisant un Set
        Set<Long> seenIds = new HashSet<>();
        List<Course> uniqueCourses = new ArrayList<>();

        for (Course course : courses) {
            if (seenIds.add(course.getId())) {
                uniqueCourses.add(course);
            }
        }

        return uniqueCourses;
    }

    public List<Course> getCoursesOfProfessor(Professor professor) {
        return courseRepository.findByProfessor(professor);
    }
}
