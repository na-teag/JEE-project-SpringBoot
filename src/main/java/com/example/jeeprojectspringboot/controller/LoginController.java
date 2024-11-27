package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, Model model) {
        // Mapping des rôles
        Map<String, String> roles = new HashMap<>();
        roles.put("admin", Admin.class.getName());
        roles.put("professor", Professor.class.getName());
        roles.put("student", Student.class.getName());

        try {
            // Vérification de l'utilisateur via le LoginManager
            Person user = loginService.authenticate(username, password);
            if (user != null) {
                // Mettre l'utilisateur et son rôle dans la session
                session.setAttribute("user", user);
                session.setAttribute("role", user.getClass().getName());
                session.setAttribute("roles", roles);

                // Rediriger vers la page d'accueil
                return "redirect:/";
            }
        } catch (IllegalAccessException e) {
            // En cas d'erreur d'authentification, retourner la page de login avec un message d'erreur
            model.addAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect.");
            return "login";
        }
        return "redirect:login";
    }
}