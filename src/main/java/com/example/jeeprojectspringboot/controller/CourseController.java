package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.CourseService;
import com.example.jeeprojectspringboot.service.GradesService;
import com.example.jeeprojectspringboot.service.ProfessorService;
import com.example.jeeprojectspringboot.service.SubjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private GradesService gradesService;
/*
    @Autowired
    private HttpServletRequest request;*/


    @GetMapping("/courses")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            session.setAttribute("courses", courseService.getAllCourses());
            session.setAttribute("subjects", subjectService.getAllSubjects()); // Ajoutez les matières
            session.setAttribute("professors", professorService.getAllProfessors()); // Ajoutez les professeurs
            return "course";
        }
        return "login";
    }


    @PostMapping("/courses")
    public String saveCourse(@RequestParam("subjectId") Long subjectId,
                             @RequestParam("professorId") Long professorId,
                             @RequestParam("classroom") String classroom,
                             @RequestParam(required = false, value = "id") Long id,
                             @RequestParam("action") String action,
                             HttpSession session) {

        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {

            if ("save".equals(action)) {
                Optional<Subject> subjectOpt = subjectService.getSubjectById(subjectId);
                Optional<Professor> professorOpt = professorService.getProfessorById(professorId);
                if (subjectOpt.isEmpty() || professorOpt.isEmpty()) {
                }
                Course course;
                if (id != null) {
                    Optional<Course> existingCourse = courseService.getCourseById(id);
                    if (existingCourse.isEmpty()) {
                    }
                    course = existingCourse.get();
                } else {
                    course = new Course();
                }
                course.setSubject(subjectOpt.get());
                course.setProfessor(professorOpt.get());
                course.setClassroom(classroom);

                courseService.saveCourse(course);
                return "redirect:/courses";

            } else if ("delete".equals(action)) {
                courseService.deleteCourse(id);
                return "redirect:/courses";
            }
            return "redirect:/courses";

        } else {
            return "error";
        }
    }
}