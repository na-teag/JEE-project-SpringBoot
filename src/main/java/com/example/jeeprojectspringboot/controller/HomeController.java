package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.Admin;
import com.example.jeeprojectspringboot.schoolmanager.Person;
import com.example.jeeprojectspringboot.schoolmanager.Professor;
import com.example.jeeprojectspringboot.schoolmanager.Student;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        // Récupérer l'utilisateur depuis la session
        Person user = (Person) session.getAttribute("user");
        String role = (String) session.getAttribute("role");
        Map<String, String> roles = (Map<String, String>) session.getAttribute("roles");

        // Si l'utilisateur est connecté, ajouter les objets 'user', role du user et roles au modèle
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("role", role);
            model.addAttribute("roles", roles);
        }

        return "index";
    }
}
