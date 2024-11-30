package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.ClassCategoryService;
import com.example.jeeprojectspringboot.service.CourseOccurenceService;
import com.example.jeeprojectspringboot.service.CourseService;
import com.example.jeeprojectspringboot.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/scheduleAdmin")
public class CourseOccurenceController {

    @Autowired
    private CourseOccurenceService courseOccurenceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClassCategoryService classCategoryService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public String showScheduleForm(Model model) {
        List<Course> courses = courseService.getAllCourses();
        List<Professor> professors = professorService.getAllProfessors();
        List<ClassCategory> categories = classCategoryService.getAllClassCategories();

        model.addAttribute("courses", courses);
        model.addAttribute("professors", professors);
        model.addAttribute("categories", categories);
        return "admin-schedule";
    }

    @PostMapping
    public String createCourseOccurrence(
            @RequestParam("day") String dayStr,
            @RequestParam("beginning") String beginningStr,
            @RequestParam("end") String endStr,
            @RequestParam("course") Long courseId,
            @RequestParam("classCategory") Long classCategoryId,
            @RequestParam(value = "professor", required = false) Long professorId,
            @RequestParam(value = "classroom", required = false) String classroom,
            Model model
    ) {
        try {
            LocalDate day = LocalDate.parse(dayStr);
            LocalTime beginning = LocalTime.parse(beginningStr);
            LocalTime end = LocalTime.parse(endStr);
            Optional<Course> existingCourse = courseService.getCourseById(courseId);
            if (existingCourse.isEmpty()) {
            }
            Course course = existingCourse.get();
            ClassCategory classCategory = classCategoryService.getClassCategory(classCategoryId);
            courseOccurenceService.createCourseOccurrence(course, day, beginning, end, classCategory);

            model.addAttribute("successMessage", "Cours enregistré avec succès !");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin-schedule";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourseOccurrence(@PathVariable("id") Long id, Model model) {
        try {
            courseOccurenceService.deleteCourseOccurence(id);
            model.addAttribute("successMessage", "Cours supprimé avec succès !");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/scheduleAdmin";
    }
}
