package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.CourseService;
import com.example.jeeprojectspringboot.service.GradesService;
import com.example.jeeprojectspringboot.service.StudentGroupService;
import com.example.jeeprojectspringboot.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GradesController {

    @Autowired
    private GradesService gradesService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentGroupService studentGroupService;

    @GetMapping("/grades")
    public String handleStudentGrades(HttpSession session, Model model) {
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


    @GetMapping("/gradesManagement")
    public String handleProfessorGradesManagement(HttpSession session, @RequestParam(value = "courses", required = false) String coursesParam,
                                                  @RequestParam(value = "classes", required = false) String classesParam,
                                                  @RequestParam(value = "students", required = false) String studentsParam, Model model) {
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Professor.class.getName())) {
            Professor professor = (Professor) session.getAttribute("user");

            // Si un cours est sélectionné
            if (coursesParam != null && !coursesParam.isEmpty()) {
                return handleCourseSelection(session, coursesParam, model);
            } else {
                return handleClassSelection(session, professor, classesParam, studentsParam, model);
            }
        } else {
            return "error";
        }
    }

    // Gérer la sélection d'un cours
    private String handleCourseSelection(HttpSession session, String coursesParam, Model model) {
        Course course = courseService.getSelectedCourse(Long.parseLong(coursesParam));

        session.setAttribute("courses", course);
        session.setAttribute("coursesId", course.getId());

        session.setAttribute("classesId", null);
        session.setAttribute("classesList", null);
        session.setAttribute("studentsList", null);
        session.setAttribute("selectedStudentGrade", null);
        session.setAttribute("selectedStudentId", null);

        model.addAttribute("courses", course);
        model.addAttribute("coursesId", course.getId());

        // Récupérer les classes associées au cours
        List<StudentGroup> classesList = course.getStudentGroups();
        session.setAttribute("classesList", classesList);
        model.addAttribute("classesList", classesList);

        return "gradesManagement";
    }


    // Gérer la sélection d'une classe et des étudiants associés
    private String handleClassSelection(HttpSession session, Professor professor, String classesParam, String studentsParam, Model model) {
        if (classesParam != null && !classesParam.isEmpty()) {
            StudentGroup studentGroup = studentGroupService.getStudentGroupFromId(Long.parseLong(classesParam));
            session.setAttribute("classes", studentGroup);
            session.setAttribute("classesId", studentGroup.getId());

            model.addAttribute("classes", studentGroup);
            model.addAttribute("classesId", studentGroup.getId());

            session.setAttribute("studentsList", null);
            session.setAttribute("selectedStudentGrade", null);
            session.setAttribute("selectedStudentId", null);

            // Récupérer la liste des étudiants en fonction du groupe
            List<Student> students = getStudentsForGroup(studentGroup);
            session.setAttribute("studentsList", students);
            model.addAttribute("studentsList", students);

            return "gradesManagement";
        } else {
            if (studentsParam != null && !studentsParam.isEmpty()) {
                return handleStudentSelection(session, studentsParam, model);
            } else {
                // Si aucun cours ni classe n'est sélectionné, afficher la liste des cours
                session.setAttribute("coursesId",null);
                session.setAttribute("classesId", null);
                session.setAttribute("classesList", null);
                session.setAttribute("studentsList", null);
                session.setAttribute("selectedStudentGrade", null);
                session.setAttribute("selectedStudentId", null);
                List<Course> coursesList = courseService.getCoursesOfProfessor(professor);
                session.setAttribute("coursesList", coursesList);
                model.addAttribute("coursesList", coursesList);
                return "gradesManagement";
            }
        }

    }

    // Récupérer les étudiants pour un groupe donné (classe, promo ou parcours)
    private List<Student> getStudentsForGroup(StudentGroup studentGroup) {
        if (studentGroup instanceof Classe) {
            return studentService.findStudentByClasseId(studentGroup.getId());
        } else if (studentGroup instanceof Promo) {
            return studentService.findStudentByPromoId(studentGroup.getId());
        } else if (studentGroup instanceof Pathway) {
            return studentService.findStudentByPathwayId(studentGroup.getId());
        }
        return List.of();
    }

    // Gérer la sélection d'un étudiant et la récupération de sa note
    private String handleStudentSelection(HttpSession session, String studentId, Model model) {
        Student selectedStudent = studentService.findStudentById(Long.parseLong(studentId));
        Grade grade = gradesService.getGradeForStudentAndForOneCourse(selectedStudent, (Course) session.getAttribute("courses"));
        session.setAttribute("selectedStudentGrade", grade);
        session.setAttribute("selectedStudentId", studentId);

        model.addAttribute("selectedStudentGrade", grade);
        model.addAttribute("selectedStudentId", studentId);
        return "gradesManagement";
    }


    private double getAverageForStudent(List<Grade> grades) {
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getResult();
        }
        return sum / grades.size();
    }

    @PostMapping("/gradesManagement")
    public String handleGradeSubmission(HttpSession session,
                                        @RequestParam(value = "coursesId") String courseId,
                                        @RequestParam(value = "selectedStudentId") String studentId,
                                        @RequestParam(value = "selectedStudentGradeId", required = false) String selectedStudentGradeId,
                                        @RequestParam(value = "grade") String grade,
                                        @RequestParam(value = "context", required = false) String context,
                                        @RequestParam(value = "comment", required = false) String comment,
                                        @RequestParam(value = "session") String sessionParam,
                                        Model model) {

        // Vérifier que l'utilisateur est un professeur
        if (session.getAttribute("user") != null && session.getAttribute("role").equals(Professor.class.getName())) {

            if (studentId != null && !studentId.isEmpty() && grade != null && !grade.isEmpty()
                   && courseId != null && !courseId.isEmpty() && sessionParam != null && !sessionParam.isEmpty()) {
                try {
                    double result = Double.parseDouble(grade);
                    Student selectedStudent = studentService.findStudentById(Long.parseLong(studentId));
                    Course selectedCourse = courseService.getSelectedCourse(Long.parseLong(courseId));
                    String message;

                    if (selectedStudentGradeId == null || selectedStudentGradeId.isEmpty() ) {
                        // Créer une nouvelle note pour l'étudiant
                        Grade newGrade = new Grade();
                        newGrade.setStudent(selectedStudent);
                        newGrade.setCourse(selectedCourse);
                        newGrade.setResult(result);
                        newGrade.setContext(context);
                        newGrade.setComment(comment);
                        newGrade.setSession(Integer.parseInt(sessionParam));
                        message = gradesService.createGrade(newGrade);
                        session.setAttribute("selectedStudentGrade", newGrade);
                        model.addAttribute("selectedStudentGrade", newGrade);
                    } else {
                        // Modifier la note existante
                        Grade oldGrade = gradesService.getGradeById(Long.parseLong(selectedStudentGradeId));
                        message = gradesService.modifyGrade(oldGrade, context, comment, Integer.parseInt(sessionParam), result);
                        session.setAttribute("selectedStudentGrade", oldGrade);
                        model.addAttribute("selectedStudentGrade", oldGrade);
                    }

                    // Enregistrer le message de confirmation
                    session.setAttribute("message", message);
                    model.addAttribute("message", message);

                    return "gradesManagement";  // Nom de la vue à afficher

                } catch (NumberFormatException e) {
                    // En cas d'erreur de conversion de note
                    session.setAttribute("error", "La note doit être un nombre valide.");
                    model.addAttribute("error", "La note doit être un nombre valide.");
                    return "gradesManagement";
                }
            } else {
                // Si l'étudiant ou la note n'est pas fourni
                session.setAttribute("error", "Veuillez sélectionner un étudiant et entrer une note.");
                model.addAttribute("error", "Veuillez sélectionner un étudiant et entrer une note.");
                return "gradesManagement";
            }
        } else {
            return "error";
        }
    }
}
