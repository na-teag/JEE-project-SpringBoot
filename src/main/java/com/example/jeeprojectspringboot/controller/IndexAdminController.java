package com.example.jeeprojectspringboot.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class IndexAdminController {
	@GetMapping("/indexAdmin")
	public String contact(){
		return "indexAdmin";
	}
}