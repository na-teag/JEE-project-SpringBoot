package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.SubjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/subjects")
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            try{
                model.addAttribute("subjects", subjectService.getAllSubjects());
                return "subject";
            } catch (Exception e){
                model.addAttribute("errorMessage", e.getMessage());
                return "subject";
            }
        }
        return "login";
    }

    @GetMapping("/subject")
    public String logout(Model model,HttpSession session, @RequestParam("action") String action, @RequestParam("name") String name, @RequestParam(value = "id", required = false) Long id) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            try{
                if ("save".equals(action)) {
                    if (id == null) {
                        // if there is no id, then it is a new object
                        Subject subject = new Subject();
                        subject.setName(name);
                        subjectService.save(subject);
                    } else {
                        // if there is an id the object already exists
                        Subject subject = subjectService.getSubject(id);
                        subject.setName(name);
                        subjectService.save(subject);
                    }
                } else if ("delete".equals(action)) {
                    subjectService.deleteSubject(id);
                } else {
                    model.addAttribute("errorMessage", "Requête non reconnue");
                    return "subject";
                }
                return "redirect:/subjects";
            } catch (Exception e){
                e.printStackTrace();
                model.addAttribute("errorMessage", e.getMessage());
                return "subject";
            }
        } else {
            return "error";
        }
    }
}
