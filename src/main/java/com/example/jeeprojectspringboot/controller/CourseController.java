package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentGroupService studentGroupService;


    @GetMapping("/courses")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            setModelAttributes(model);
            return "course";
        }
        return "login";
    }

    private void setModelAttributes(Model model){
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("subjects", subjectService.getAllSubjects()); // Ajoutez les matières
        model.addAttribute("professors", professorService.getAllProfessors()); // Ajoutez les professeurs
        model.addAttribute("groups", studentGroupService.getAllStudentGroups()); // Ajouter les groupes
    }

    @PostMapping("/courses")
    public String saveCourse(@RequestParam("subjectId") Long subjectId,
                             @RequestParam("professorId") Long professorId,
                             @RequestParam("classroom") String classroom,
                             @RequestParam(required = false, value = "id") Long id,
                             @RequestParam("action") String action,
                             @RequestParam("groupsId") List<Long> groupsId,
                             HttpSession session,
                             Model model) {

        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {

            if ("save".equals(action)) {
                Subject subject = subjectService.getSubject(subjectId);
                Professor professor = professorService.findProfessorById(professorId);
                if (subject == null || professor == null) {
                    model.addAttribute("errorMessage","Sujet ou professeur non valide");
                    setModelAttributes(model);
                    return "course";
                }
                Course course;
                if (id != null) {
                    Course existingCourse = courseService.getSelectedCourse(id);
                    if (existingCourse == null) {
                        model.addAttribute("errorMessage","cours non valide");
                        setModelAttributes(model);
                        return "course";
                    }
                    course = existingCourse;
                } else {
                    course = new Course();
                }
                if (!professor.getTeachingSubjects().contains(subject)) {
                    model.addAttribute("errorMessage", "Ce professeur ne peut pas enseigner cette matière");
                    setModelAttributes(model);
                    return "course";
                }
                List<StudentGroup> studentGroups = new ArrayList<>();
                StudentGroup studentGroup;
                for (Long groupId : groupsId) {
                    studentGroup = studentGroupService.getStudentGroupFromId(groupId);
                    if (studentGroup == null) {
                        model.addAttribute("errorMessage", "Groupes non valides");
                        setModelAttributes(model);
                        return "course";
                    }
                    List<Course> coursesBySubject = courseService.getCoursesBySubject(subject);
                    for(Course course1 : coursesBySubject){
                        if(course1.getSubject().equals(subject)){
                            if(course.getId() == null) {
                                if (course1.getStudentGroups().contains(studentGroup)) {
                                    model.addAttribute("errorMessage", "Au moins un des groupes sélectionnés a déja ce cours avec un autre professeur");
                                    setModelAttributes(model);
                                    return "course";
                                } else if (course1.getProfessor().equals(professor)) {
                                    model.addAttribute("errorMessage", "Au moins un des groupes sélectionnés a déja ce cours avec ce professeur");
                                    setModelAttributes(model);
                                    return "course";
                                } else if(studentGroup instanceof Classe){
                                    Classe classe = (Classe) studentGroup;
                                    if(course1.getStudentGroups().contains(classe.getPromo()) || course1.getStudentGroups().contains(classe.getPathway())){
                                        model.addAttribute("errorMessage", "Au moins un des groupes sélectionnés a déja ce cours avec ce professeur qui enseigne à toute une filière ou une promo");
                                        setModelAttributes(model);
                                        return "course";
                                    }
                                }
                            } else {
                                if (course1.getStudentGroups().contains(studentGroup) && course.getProfessor().equals(professor)) {
                                    model.addAttribute("errorMessage", "Au moins un des groupes sélectionnés a déja ce cours avec un autre professeur");
                                    setModelAttributes(model);
                                    return "course";
                                } else if(studentGroup instanceof Classe){
                                    Classe classe = (Classe) studentGroup;
                                    if(course1.getStudentGroups().contains(classe.getPromo()) || course1.getStudentGroups().contains(classe.getPathway())){
                                        model.addAttribute("errorMessage", "Au moins un des groupes sélectionnés a déja ce cours avec ce professeur qui enseigne à toute une filière ou une promo");
                                        setModelAttributes(model);
                                        return "course";
                                    }
                                }
                            }
                        }
                    }
                    studentGroups.add(studentGroup);
                }

                course.setStudentGroups(studentGroups);
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

    private boolean isSamePromoOrPathway(Course course1, StudentGroup studentGroup, Professor professor) {
        if (studentGroup instanceof Promo) {
            Promo promo = (Promo) studentGroup;
            if (course1.getStudentGroups().contains(promo) && course1.getProfessor().equals(professor)) {
                return true;
            }
        } else if (studentGroup instanceof Pathway) {
            Pathway pathway = (Pathway) studentGroup;
            if (course1.getStudentGroups().contains(pathway) && course1.getProfessor().equals(professor)) {
                return true;
            }
        }
        return false;
    }
}