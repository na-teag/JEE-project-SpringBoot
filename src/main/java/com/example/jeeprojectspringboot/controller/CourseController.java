package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.Admin;
import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Professor;
import com.example.jeeprojectspringboot.schoolmanager.Subject;
import com.example.jeeprojectspringboot.service.CourseService;
import com.example.jeeprojectspringboot.service.ProfessorService;
import com.example.jeeprojectspringboot.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/courses")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            session.setAttribute("courses", courseService.getAllCourses());
            session.setAttribute("subjects", subjectService.getAllSubjects()); // Ajoutez les matières
            session.setAttribute("professors", professorService.getAllProfessors()); // Ajoutez les professeurs
            return "courses";
        }
        return "login";
    }


    @GetMapping("/course")
    public String saveCourse(@RequestParam Long subjectId,
                                        @RequestParam Long professorId,
                                        @RequestParam String classroom,
                                        @RequestParam(required = false) Long id,
                                        @RequestParam("action") String action){

        if ("save".equals(action)) {
            Optional<Subject> subjectOpt = subjectService.getSubjectById(subjectId);
            Optional<Professor> professorOpt = professorService.getProfessorById(professorId);
            if (subjectOpt.isEmpty() || professorOpt.isEmpty()) {
                return "courses";
            }
            Course course;
            if (id != null) {
                Optional<Course> existingCourse = courseService.getCourseById(id);
                if (existingCourse.isEmpty()) {
                    return "courses";
                }
                course = existingCourse.get();
            } else {
                course = new Course();
            }
            course.setSubject(subjectOpt.get());
            course.setProfessor(professorOpt.get());
            course.setClassroom(classroom);

            courseService.saveCourse(course);
            return "courses";
        }else if ("delete".equals(action)){
            courseService.deleteCourse(id);
        }
        return "courses";
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
