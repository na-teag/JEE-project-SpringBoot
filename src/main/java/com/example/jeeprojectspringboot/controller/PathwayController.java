package com.example.jeeprojectspringboot.controller;

import com.example.jeeprojectspringboot.schoolmanager.*;
import com.example.jeeprojectspringboot.service.PathwayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PathwayController {

	@Autowired
	private PathwayService pathwayService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/pathways")
	public String login(HttpSession session) {
		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
			session.setAttribute("pathways", pathwayService.getAllPathways());
			return "pathway";
		}
		return "login";
	}

	@GetMapping("/pathway")
	public String logout(HttpSession session, @RequestParam("action") String action, @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam(value = "id", required = false) Long id) {
		try{
			if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))){
				if ("save".equals(action)) {
					if (id == null) {
						// if there is no id, then it is a new object
						Pathway pathway = new Pathway();
						pathway.setName(name);
						pathway.setEmail(email);
						pathwayService.save(pathway);
					} else {
						// if there is an id the object already exists
						Pathway pathway = pathwayService.getPathway(id);
						pathway.setName(name);
						pathway.setEmail(email);
						pathwayService.save(pathway);
					}
				} else if ("delete".equals(action)) {
					pathwayService.deletePathway(id);
				} else {
					request.setAttribute("errorMessage", "Requête non reconnue");
					return "pathway";
				}
				return "redirect:/pathways";
			} else {
				return "error";
			}
		} catch (Exception e){
			request.setAttribute("errorMessage", e.getMessage());
			return "pathway";
		}
	}
}
