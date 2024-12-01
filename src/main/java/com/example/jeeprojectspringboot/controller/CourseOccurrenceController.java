package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class CourseOccurrenceController {

    @Autowired
    private CourseOccurrenceService courseOccurrenceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClassCategoryService classCategoryService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/CourseOccurrences")
    public String showScheduleForm(Model model, HttpSession session) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            List<CourseOccurrence> courseOccurrences = courseOccurrenceService.getAllCourseOccurrence();
            List<Course> courses = courseService.getAllCourses();
            List<Professor> professors = professorService.getAllProfessors();
            List<ClassCategory> categories = classCategoryService.getAllClassCategories();

            model.addAttribute("courseOccurrences", courseOccurrences);
            model.addAttribute("courses", courses);
            model.addAttribute("professors", professors);
            model.addAttribute("categories", categories);
            model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return "CourseOccurrence";
        }
        return "login";
    }

    @PostMapping("/CourseOccurrences")
    public String manageCourseOccurrence(
            @RequestParam("action") String action,
            @RequestParam("day") String dayStr,
            @RequestParam("beginning") String beginningStr,
            @RequestParam("end") String endStr,
            @RequestParam("course") Long courseId,
            @RequestParam("classCategory") Long classCategoryId,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "professor", required = false) Long professorId,
            @RequestParam(value = "classroom", required = false) String newClassroom,
            Model model,
            HttpSession session
    ) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            try {
                LocalDate day = LocalDate.parse(dayStr);
                LocalTime beginning = LocalTime.parse(beginningStr);
                LocalTime end = LocalTime.parse(endStr);

                if ("save".equals(action)) {
                    courseOccurrenceService.validateSchedule(day,beginning,end);
                    Course course = courseService.getSelectedCourse(courseId);
                    ClassCategory classCategory = classCategoryService.getClassCategory(classCategoryId);
                    Professor professor;
                    if (professorId == null){
                        professor = course.getProfessor();
                    } else {
                        professor = professorService.findProfessorById(professorId);
                    }
                    String classroom;
                    if (newClassroom == ""){
                        classroom = course.getClassroom();
                    } else {
                        classroom = newClassroom;
                    }
                    CourseOccurrence courseOccurrence = new CourseOccurrence();

                    if (id == null) {
                        model.addAttribute("successMessage", "Nouvelle occurrence de cours enregistrée avec succès !");
                    } else {
                        courseOccurrence = courseOccurrenceService.getCourseOccurrenceById(id);
                        model.addAttribute("successMessage", "Occurrence de cours mise à jour avec succès !");
                    }
                    courseOccurrence.setCourse(course);
                    courseOccurrence.setProfessor(professor);
                    courseOccurrence.setClassroom(classroom);
                    courseOccurrence.setCategory(classCategory);
                    courseOccurrence.setDay(day);
                    courseOccurrence.setBeginning(beginning);
                    courseOccurrence.setEnd(end);
                    courseOccurrenceService.save(courseOccurrence);
                } else if ("delete".equals(action)) {
                    courseOccurrenceService.deleteCourseOccurrence(id);
                } else {
                    model.addAttribute("errorMessage", "Action non reconnue.");
                    return "CourseOccurrence";
                }
                return "redirect:/CourseOccurrences";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Erreur : " + e.getMessage());
                return "CourseOccurrence";
            }
        } else {
            return "error";
        }
    }

}
