package com.School.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
	 @GetMapping("/jasem")
	public String home(Model model) {
		model.addAttribute("message", "Hello, Thymeleaf!");
        return "index";
	}
}
