package com.example.jeeprojectspringboot.controller;
import com.example.jeeprojectspringboot.schoolmanager.Admin;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class IndexAdminController {
	@GetMapping("/indexAdmin")
	public String contact(HttpSession session){
		if (session.getAttribute("user") != null && Admin.class.getName().equals(session.getAttribute("role"))) {
			return "indexAdmin";
		} else {
			return "error";
		}
	}
}