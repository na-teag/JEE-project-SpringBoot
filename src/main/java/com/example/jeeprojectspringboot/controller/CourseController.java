package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.CourseService;
import com.example.jeeprojectspringboot.service.GradesService;
import com.example.jeeprojectspringboot.service.ProfessorService;
import com.example.jeeprojectspringboot.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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
                             HttpSession session,
                             Model model) {

        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {

            if ("save".equals(action)) {
                Subject subject = subjectService.getSubject(subjectId);
                Professor professor = professorService.findProfessorById(professorId);
                if (subject == null || professor == null) {
                    model.addAttribute("errorMessage","Sujet ou professeur non valide");
                    return "course";
                }
                Course course;
                if (id != null) {
                    Course existingCourse = courseService.getSelectedCourse(id);
                    if (existingCourse == null) {
                        model.addAttribute("errorMessage","cours non valide");
                        return "course";
                    }
                    course = existingCourse;
                } else {
                    course = new Course();
                }
                if (!professor.getTeachingSubjects().contains(subject)) {
                    model.addAttribute("errorMessage", "Ce professeur ne peut pas enseigner cette matière");
                    return "course";
                }
                course.setSubject(subject);
                course.setProfessor(professor);
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