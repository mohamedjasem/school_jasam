package com.School.SecurityController;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.School.SecurityRequest.AuthenticationRequest;
import com.School.SecurityRequest.RegisterRequest;
import com.School.SecurityResponse.AuthenticationResponse;
import com.School.SecurityService.UserService;

import lombok.RequiredArgsConstructor;
//localhost:8080/school/user/create
@CrossOrigin("*")
@RestController
@RequestMapping("/v1/school/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @PostMapping("/create")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authResponse = userService.register(registerRequest);
        return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest loginRequest){
    	 AuthenticationResponse loginResponse = userService.authenticateUser(loginRequest);
    	return ResponseEntity.ok(loginResponse);
    }
    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
    @GetMapping("/test")
    public String test() {
    	return "welcome";
    }
    @GetMapping("/home")
    public ResponseEntity<String> welcome() {
        // Return HTML content as a string
        String htmlContent = "welcom";
        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }
}

