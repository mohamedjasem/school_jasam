package com.School.SecurityService;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.School.SecurityEntity.User; // Correct import for User entity
import com.School.SecurityRepository.UserRepository;
import com.School.SecurityRequest.AuthenticationRequest;
import com.School.SecurityRequest.RegisterRequest;
import com.School.SecurityResponse.AuthenticationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository; // Using constructor injection
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	

	public AuthenticationResponse register(RegisterRequest registerRequest) {
		User user = User.builder().firstName(registerRequest.getFirstName()).lastName(registerRequest.getLastName())
				.email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword()))
				.role(registerRequest.getRole()).build();

		User savedUser = userRepository.save(user);
		String jwtToken = jwtService.generateToken(user);

		return AuthenticationResponse.builder().accessToken(jwtToken).build();
	}
	
	public AuthenticationResponse authenticateUser(AuthenticationRequest loginrequest){
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginrequest.getUsername(), loginrequest.getPassword()));
		
		User user=userRepository.findByEmail(loginrequest.getUsername())
				.orElseThrow();
		String jwtToken=jwtService.generateToken(user);
		
		return AuthenticationResponse.builder().accessToken(jwtToken).build();
		
	}
}
