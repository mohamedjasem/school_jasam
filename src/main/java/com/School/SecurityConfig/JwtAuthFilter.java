package com.School.SecurityConfig;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.School.SecurityService.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String jwt;
		final String email;
		final String authHeader=request.getHeader("Authorization");
		if(authHeader ==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request,response);
			return;
		}
		jwt=authHeader.substring(7);//Extract jwt from the Authorization
		
		email=jwtService.extractUsername(jwt);
		if(email != null &&SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=this.userDetailsService.loadUserByUsername(email);
			
			UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
					userDetails,null,userDetails.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authToken);
			
			
			
			
			
		}
		filterChain.doFilter(request,response);
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().contains("/school/user/*");
	}

}
