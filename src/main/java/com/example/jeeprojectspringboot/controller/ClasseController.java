package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.Admin;
import com.example.jeeprojectspringboot.schoolmanager.Classe;
import com.example.jeeprojectspringboot.service.ClasseService;
import com.example.jeeprojectspringboot.service.PathwayService;
import com.example.jeeprojectspringboot.service.PromoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @Autowired
    private PromoService promoService;

    @Autowired
    private PathwayService pathwayService;

    @GetMapping("/classes")
    public String getClassesPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            model.addAttribute("classes", classeService.getListOfClasses());
            model.addAttribute("promos", promoService.getAllPromo());
            model.addAttribute("pathways", pathwayService.getAllPathways());
            model.addAttribute("errorMessage", "");
            return "classes";
        } else {
            return "error";
        }
    }


    @PostMapping("/classes")
    public String createOrUpdateClasse(@RequestParam("name") String name,
                                       @RequestParam("email") String email,
                                       @RequestParam("promoId") String promoId,
                                       @RequestParam("pathwayId") String pathwayId,
                                       @RequestParam("id") String classeId,
                                       @RequestParam("action") String action,
                                       HttpSession session,
                                       Model model) {

        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
            String result = null;

            if ("save".equals(action)) {
                if (classeId == null || classeId.isEmpty()) {
                    result = classeService.createClasse(name, promoId, pathwayId, email);
                } else {
                    result = classeService.updateClasseById(classeId, name, promoId, pathwayId, email);
                }
            } else if ("delete".equals(action)) {
                result = classeService.deleteClasseById(classeId);
            }

            if (result != null) {
                model.addAttribute("errorMessage", result);
            } else {
                model.addAttribute("errorMessage", "Opération réussie");
            }

            model.addAttribute("classes", classeService.getListOfClasses());
            model.addAttribute("promos", promoService.getAllPromo());
            model.addAttribute("pathways", pathwayService.getAllPathways());

            return "classes";
        } else {
            return "error";
        }
    }
}
