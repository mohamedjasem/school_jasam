package com.School.SecurityService;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.School.SecurityEntity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET = "1A735481BD466D1A3E384F2DB28C99F94637E81ED25A8F23D685823D183F05F3";
	
	public String generateToken(UserDetails user) {
		
		User userentity=(User)user;
		// Retrieve first name and last name
	    String firstName = userentity.getFirstName();
	    String lastName = userentity.getLastName();
		return Jwts.builder().setSubject(user.getUsername())
			
				.claim("authorities", populateAuthorities(user.getAuthorities()))
				.claim("name",firstName+" " +lastName)  // Add the user's name as a claim
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : authorities) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}

	private Key getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(SECRET);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractAllClaims(String token) {
	    return Jwts.parserBuilder()
	               .setSigningKey(getSigningKey())
	               .build()  // Build the JwtParser
	               .parseClaimsJws(token)  // Parse the JWT token
	               .getBody();  // Get the JWT claims
	}
	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public <T> T extractClaim(String token,Function<Claims, T> claimsResolver){
		final Claims claims=extractAllClaims(token) ;
		return claimsResolver.apply(claims);
	}
	
	public boolean isTokenVaild(String token,UserDetails userDetails) {
		final String username=extractUsername(token);
		return (username.equals(userDetails.getUsername()));
	}
	
	
	
	
	
	
	
	
}
