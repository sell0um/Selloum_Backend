package com.selloum.api.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.selloum.api.auth.domain.CustomUserDetails;
import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;
import com.selloum.domain.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret-key}")
	private String secret;
	@Value("${jwt.prefix}")
	private String prefix;
	@Value("${jwt.issuer}")
	private String issuer;
	@Value("${jwt.expiration.access}")
	private String accessTokenExpiredTime;
	@Value("${jwt.expiration.refresh}")
	private String refreshTokenExpiredTime;
	
	public String generateToken(String category, String username, String role) {
		
		Date expiration = (category.equals("access") ? new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpiredTime)) : new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiredTime)));
		
		
		return Jwts.builder()
				.claim("category", category)
				.claim("username", username)
				.claim("role", role)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expiration)
				.signWith(getSigningKey())
				.compact();
	}
	
	public String getTokenWithPrefix(String token) {
		return  prefix+ " " + token;
	}
	
	public String getTokenWithoutPrefix(String token) {
		return token.substring(prefix.length() + 1);
	}
	
	public boolean isStartWithPrfix(String token) {
		return token.startsWith(prefix + " ");
	}
	
	public boolean validateToken(String token) {
		try {
			
		     Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
	            return true;
	        } catch (ExpiredJwtException e) {
	            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
	        } catch (JwtException | IllegalArgumentException e) {
	            throw new CustomException(ErrorCode.INVALID_TOKEN);
	        }
	}
	
	public String getUsername(String token) {
		
		return parseClaims(token).get("username", String.class);
	}
	
	public String getRole(String token) {
		return parseClaims(token).get("role", String.class);
	}
	
	public Long getExprired(String token) {
		return parseClaims(token).get("role", Long.class);
	}
	
	public boolean isExpired(String token) {
		return parseClaims(token).getExpiration().before(new Date());
	}
	
    public Authentication getAuthentication(String token) {
        // 1.⃣ 토큰에서 username 추출
        String username = getUsername(token);

        // 2. 토큰에서 Role 추출
        String role = getRole(token);
        
        // 3 authorities 설정
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // 4 Spring Security가 이해하는 UserDetails 형태로 구성
        User user = User.builder()
                .username(username)
                .role(role)
                .password("") // JWT에는 비밀번호 없음
                .build();
        
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 5 Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
	
    // Claims 파싱
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public long getAccessTokenExpiration() {
    	return Long.parseLong(accessTokenExpiredTime);
    }
    
    public long getRefreshTokenExpiration() {
    	return Long.parseLong(refreshTokenExpiredTime);
    }
	
    
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	
	
	
}
