package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;

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

    @Autowired
    private ClasseService classeService;


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
                    studentGroups.add(studentGroup);
                }


                /*
                 * vérifier que deux profs différents ne peuvent pas enseigner le même sujet a une même classe, ou qu'une filière/promo n'a pas deux fois le même sujet de cours
                 * le cas multiple autorisé étant qu'un prof d'info donne un cours à une Promo ou à une filière, et en plus, qu'il fasse cours des classes (des cours de CM à toute la filière et des cours de TD à une classe particulière, par exemple)
                 */
                course.setStudentGroups(studentGroups);
                course.setSubject(subject);
                course.setProfessor(professor);
                course.setClassroom(classroom);
                // ne pas sauvegarder pour l'instant

                for (StudentGroup studentGroup1 : studentGroups){
                    if (Promo.class.getName().equals(studentGroup1.getClass().getName()) || Pathway.class.getName().equals(studentGroup1.getClass().getName())){
                        for (Course course1 : courseService.getCoursesBySubject(course.getSubject())) {
                            List<Long> coursesId = new ArrayList<>();
                            for (Course course2 : courseService.getCoursesByStudentGroup(studentGroup1)){
                                coursesId.add(course2.getId());
                            }
                            if (coursesId.contains(course1.getId())) {
                                model.addAttribute("errorMessage", "une filière ou une promo ne peut pas avoir le même sujet de cours plusieurs fois");
                                setModelAttributes(model);
                                return "course";
                            }
                        }
                    }
                }


                // identifier toutes les classes concernées (dans un set pour éviter les doublons)
                Set<Classe> uniqueClasses = new HashSet<>();
                for (StudentGroup group : studentGroups) {
                    uniqueClasses.addAll(classeService.getClassesByStudentGroup(group));
                }

                // pour chaque classe
                for(Classe classe : uniqueClasses) {
                    Map<Long, Long> subjectProfessorMap = new HashMap<>();
                    List<Course> courses = new ArrayList<>(courseService.getCoursesByClasse(classe));
                    courses.add(course); // ajouter le cours dont on cherche à tester s'il est correcte
                    // dans chaque cours
                    for (Course course1 : courses) {
                        // regarder quel professeur fait le cours du sujet
                        if (subjectProfessorMap.containsKey(course1.getSubject().getId()) && !Objects.equals(subjectProfessorMap.get(course1.getSubject().getId()), course1.getProfessor().getId())) {
                            // si on a déjà repertorié ce sujet avec un professeur différent, erreur
                            model.addAttribute("errorMessage", "une classe ne peut pas avoir le même sujet enseigné par deux professeurs différents");
                            setModelAttributes(model);
                            return "course";
                        }
                        // sinon, répertorier le sujet et son professeur
                        subjectProfessorMap.put(course1.getSubject().getId(), course1.getProfessor().getId());
                    }
                }


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