package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.ClassCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class ClassCategoryController {

    @Autowired
    private ClassCategoryService classCategoryService;

    @GetMapping("/classCategories")
    public String getClassCategories(HttpSession session, Model model) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            try{
                model.addAttribute("classCategories", classCategoryService.getAllClassCategories());
                return "classCategory";
            } catch (Exception e){
                model.addAttribute("errorMessage", e.getMessage());
                return "classCategory";
            }
        }
        return "login";
    }

    @PostMapping("/classCategories")
    public String editClassCategory(Model model,HttpSession session, @RequestParam("action") String action, @RequestParam("name") String name, @RequestParam("color") String color, @RequestParam(value = "id", required = false) Long id) {
        if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
            try{
                if ("save".equals(action)) {
                    if (id == null) {
                        // if there is no id, then it is a new object
                        ClassCategory classCategory = new ClassCategory();
                        classCategory.setName(name);
                        classCategory.setColor(color);
                        classCategoryService.save(classCategory);
                    } else {
                        // if there is an id the object already exists
                        ClassCategory classCategory = classCategoryService.getClassCategory(id);
                        classCategory.setName(name);
                        classCategory.setColor(color);
                        classCategoryService.save(classCategory);
                    }
                } else if ("delete".equals(action)) {
                    classCategoryService.deleteClassCategory(id);
                } else {
                    model.addAttribute("errorMessage", "Requête non reconnue");
                    return "classCategory";
                }
                return "redirect:/classCategories";
            } catch (Exception e){
                e.printStackTrace();
                model.addAttribute("errorMessage", e.getMessage());
                return "classCategory";
            }
        } else {
            return "error";
        }
    }
}

