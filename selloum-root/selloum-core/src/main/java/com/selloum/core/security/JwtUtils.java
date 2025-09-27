package com.selloum.core.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * 
 * @author hongseok
 * @since 09/15/25
 */
public class JwtUtils {
	
	public static Claims getClaims(String token, Key key) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public static String getUserName(String token, Key key) {
		return getClaims(token, key).get("username", String.class);
	}
	
	public static String getCategory(String token, Key key) {
		return getClaims(token, key).get("category", String.class);
	}
	
	public static String getRoleList(String token, Key key) {
		return getClaims(token, key).get("role", String.class);
	}
	
	public static Date getExpiration(String token, Key key) {
		return getClaims(token, key).getExpiration();
	}
	
	public static boolean isTokenExpired(String token, Key key) {
		return getExpiration(token, key).before(new Date());
				
	}

}
