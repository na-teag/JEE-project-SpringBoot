package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.PromoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class PromoController {

    @Autowired
    private PromoService promoService;

    @GetMapping("/promos")
    public String getPromo(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            try{
                model.addAttribute("promos", promoService.getAllPromo());
                return "promo";
            } catch (Exception e){
                model.addAttribute("errorMessage", e.getMessage());
                return "promo";
            }
        }
        return "error";
    }

    @PostMapping("/promos")
    public String createOrUpdatePromo(Model model,HttpSession session, @RequestParam("action") String action, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam(value = "id", required = false) Long id) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            try{
                if ("save".equals(action)) {
                    if (id == null) {
                        // if there is no id, then it is a new object
                        Promo promo = new Promo();
                        promo.setName(name);
                        promo.setEmail(email);
                        promoService.save(promo);
                    } else {
                        // if there is an id the object already exists
                        Promo promo = promoService.getPromo(id);
                        promo.setName(name);
                        promo.setEmail(email);
                        promoService.save(promo);
                    }
                } else if ("delete".equals(action)) {
                    promoService.deletePromo(id);
                } else {
                    model.addAttribute("errorMessage", "Requête non reconnue");
                    return "promo";
                }
                return "redirect:/promos";
            } catch (Exception e){
                e.printStackTrace();
                model.addAttribute("errorMessage", e.getMessage());
                return "promo";
            }
        } else {
            return "error";
        }
    }
}
