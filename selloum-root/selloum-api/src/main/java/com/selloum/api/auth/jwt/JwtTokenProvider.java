package com.selloum.api.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

	private Key key;
	@Value("$jwt.secret-key")
	private String secret;
	@Value("$jwt.prefix")
	private String prefix;
	@Value("$jwt.issuer")
	private String issuer;
	@Value("$jwt.expiration.access")
	private String accessTokenExpiredTime;
	@Value("$jwt.expiration.refresh")
	private String refreshTokenExpiredTime;
	
	public String generateAccessToken(String category, String username, String role) {
		
		Date expiration = (category.equals("access") ? new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpiredTime)) : new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiredTime)));
		
		return Jwts.builder()
				.claim("category", category)
				.claim("username", username)
				.claim("role", role)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expiration)
				.signWith(key)
				.compact();
	}
	
	public void validateToken(String token) {
		
	}
	
	
	
	
	
}
