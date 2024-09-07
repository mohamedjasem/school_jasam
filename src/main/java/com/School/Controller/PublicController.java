package com.School.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the public home page!";
    }

    @GetMapping("/about")
    public String about() {
        return "This is the about page accessible to everyone.";
    }
}
