package com.example.jeeprojectspringboot.service;

import com.example.jeeprojectspringboot.repository.GradesRepository;
import com.example.jeeprojectspringboot.schoolmanager.Course;
import com.example.jeeprojectspringboot.schoolmanager.Grade;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class GradesService {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private MailService mailService;

    public Grade getGradeById(long id){
        return gradesRepository.findById(id);
    }

    public List<Grade> getGradesForStudent(Student student) {
        return gradesRepository.findByStudent(student);
    }

    @Transactional
    public void deleteGradesForStudent(Student student) {
        gradesRepository.deleteByStudent(student);
    }

    public Grade getGradeForStudentAndForOneCourse(Student student, Course course) {
        return gradesRepository.findByStudentAndCourse(student, course);
    }

    @Transactional
    public String createGrade(Grade grade) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Valider l'objet Grade
        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);

        // Si des violations sont présentes, renvoyer les erreurs
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Grade> violation : violations) {
                errorMessages.append(violation.getMessage()).append(" ");
            }
            return errorMessages.toString(); // Retourne les erreurs de validation
        }

        try {
            // Enregistrement de la note via JpaRepository (Spring Data JPA)
            gradesRepository.save(grade);

            // Envoi d'email de notification
            String subject = grade.getCourse().getSubject().getName();
            String emailContent = "Bonjour, Vous recevez cet email car une nouvelle note concernant la matière " + subject + ".\nConsultez votre note sur l'ENT.\n\nBien cordialement, le service administratif.\n\nP.-S. Merci de ne pas répondre à ce mail";
            mailService.sendEmail("do.not.reply@cytech.fr", grade.getStudent(), "Nouvelle note disponible", emailContent);

            return "La note a été enregistrée avec succès.";
        } catch (Exception e) {
            return "Erreur lors de l'enregistrement de la note : " + e.getMessage();
        }
    }

    @Transactional
    public String modifyGrade(Grade grade, String context, String comment, int session, double result) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Valider l'objet Grade
        Set<ConstraintViolation<Grade>> violations = validator.validate(grade);

        // Si des violations sont présentes, renvoyer les erreurs
        if (!violations.isEmpty()) {
            StringBuilder errorMessages = new StringBuilder();
            for (ConstraintViolation<Grade> violation : violations) {
                errorMessages.append(violation.getMessage()).append(" ");
            }
            return errorMessages.toString(); // Retourne les erreurs de validation
        }

        try {
            // Modifier les propriétés de l'objet Grade
            grade.setContext(context);
            grade.setComment(comment);
            grade.setSession(session);
            grade.setResult(result);

            // Mettre à jour l'objet Grade dans la base de données
            gradesRepository.save(grade);

            return "La note a été modifiée avec succès.";
        } catch (Exception e) {
            return "Erreur lors de la modification de la note : " + e.getMessage();
        }
    }
    public void deleteById(Long id){gradesRepository.deleteById(id);}

    public List<Grade> findByCourse(Course course) {
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException("Course must not be null and must have a valid ID.");
        }
        return gradesRepository.findByCourse(course);
    }
}
