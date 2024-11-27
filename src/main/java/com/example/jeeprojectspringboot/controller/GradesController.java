package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Grade;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import com.example.jeeprojectspringboot.service.CourseService;
import com.example.jeeprojectspringboot.service.GradesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GradesController {

    @Autowired
    private GradesService gradesService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/grades")
    public String handleStudentGrades(HttpSession session, Model model){
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Student.class.getName())) {
            Student student = (Student) session.getAttribute("user");

            // Récupérer les notes de l'étudiant
            List<Grade> grades = gradesService.getGradesForStudent(student);
            if (!grades.isEmpty()) {
                double average = getAverageForStudent(grades);
                session.setAttribute("average", average);
                model.addAttribute("average", average);
            }

            // Récupérer les cours associés à l'étudiant
            List<Course> courses = courseService.getCoursesOfStudent(student);
            session.setAttribute("courses", courses);
            session.setAttribute("grades", grades);

            model.addAttribute("courses", courses);
            model.addAttribute("grades", grades);

            // Rediriger vers la vue des grades
            return "grades";
        } else {
            return "error";
        }
    }

    /*
    @GetMapping("/gradesManagement")
    public void handleProfessorGradesManagement(){

    }
*/

    private double getAverageForStudent(List<Grade> grades) {
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getResult();
        }
        return sum / grades.size();
    }
}
