package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.CourseRepository;
import com.example.jeeprojectspringboot.schoolmanager.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradesService gradesService;

    @Autowired
    @Lazy
    private CourseOccurrenceService courseOccurrenceService;


    public List<Course> getCoursesOfStudent(Student student) {
        Classe studentClasse = student.getClasse();
        Promo studentPromo = studentClasse.getPromo();
        Pathway studentPathway = studentClasse.getPathway();

        List<Course> courses = new ArrayList<>();

        // Récupération des cours pour la classe de l'étudiant
        List<Course> tmp = courseRepository.findByStudentGroupsContaining(studentClasse);
        if (tmp != null) {
            courses.addAll(tmp);
        }

        // Récupération des cours pour la promo de l'étudiant
        tmp = courseRepository.findByStudentGroupsContaining(studentPromo);
        if (tmp != null) {
            courses.addAll(tmp);
        }

        // Récupération des cours pour le pathway de l'étudiant
        tmp = courseRepository.findByStudentGroupsContaining(studentPathway);
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

    public Course getSelectedCourse(long id){
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getCoursesBySubject(Subject subject) { return courseRepository.findBySubject(subject); }

    public List<Course> getCoursesByClasse(Classe classe) {
        List<Course> courses = new ArrayList<>();
        courses.addAll(courseRepository.findByStudentGroupsContaining(classe));
        courses.addAll(courseRepository.findByStudentGroupsContaining(classe.getPathway()));
        courses.addAll(courseRepository.findByStudentGroupsContaining(classe.getPromo()));

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


    public Course saveCourse(Course course) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Course>> errors = validator.validate(course);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.getReferenceById(id).setStudentGroups(new ArrayList<>());
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            List<Grade> grades = gradesService.findByCourse(course);
            for (Grade grade : grades) {
                gradesService.deleteById(grade.getId());
            }
            List<CourseOccurrence> courseOccurrences = courseOccurrenceService.findByCourse(course);
            for (CourseOccurrence courseOccurrence : courseOccurrences){
                courseOccurrenceService.deleteCourseOccurrence(courseOccurrence.getId());
            }
            courseRepository.delete(course);
        } else {
            throw new EntityNotFoundException("Course with ID " + id + " not found");
        }
    }
}