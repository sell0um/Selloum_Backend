package com.selloum.api.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.selloum.api.auth.domain.CustomUserDetails;
import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;
import com.selloum.domain.entity.User;
import com.selloum.domain.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	
	private final UserRepository userRepository;

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
	
	
	/**
	 *  generateToken(String category, String username, String role) - 접근 / 갱신 토큰 생성 메소드
	 * 
	 * 
	 * @param category
	 * @param username
	 * @param role
	 * @return
	 */
	public String generateToken(String category, long userId, String role) {
		
		Date expiration = (category.equals("access") ? new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpiredTime)) : new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiredTime)));
		
		
		return Jwts.builder()
				.claim("category", category)
				.claim("userId", userId)
				.claim("role", role)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expiration)
				.signWith(getSigningKey())
				.compact();
	}
	
	
	/**
	 * getTokenWithPrefix(String token) - 접두사 + Token 생성 메소드
	 * 
	 * 
	 * @param token
	 * @return
	 */
	public String getTokenWithPrefix(String token) {
		return  prefix+ " " + token;
	}
	
	/**
	 * getTokenWithoutPrefix(String token) - 접두사 제거 Tokne 추출 메소드
	 * 
	 * @param token
	 * @return
	 */
	
	public String getTokenWithoutPrefix(String token) {
		return token.substring(prefix.length() + 1);
	}
	
	/**
	 * 
	 * isStartWithPrfix(String token) - 설정한 접두사로 시작된 트큰인지 검증하는 메소드
	 * 
	 * @param token
	 * @return
	 */
	public boolean isStartWithPrfix(String token) {
		return token.startsWith(prefix + " ");
	}
	
	
	/**
	 * 
	 * validateToken(String token) - 토큰 유효성 검사 메소드
	 * 
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token) {
		try {
			
		     Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
	            return true;
	        } catch (ExpiredJwtException e) {
	            throw e;
	        } catch (JwtException | IllegalArgumentException e) {
	            throw e;
	        }
	}
	
	/**
	 * isExpired(String token) - 토큰 만료 여부 확인 메소드
	 * 
	 * @param token
	 * @return
	 */
	
	public boolean isExpired(String token) {
		return parseClaims(token).getExpiration().before(new Date());
	}
	
	
	/**
	 * getUsername() / getRole() / getExpried() 토큰에서 특정 Claims 추출 메소드
	 * 
	 */
	
	public Long getUserId(String token) {
		
		return parseClaims(token).get("userId", Long.class);
	}
	
	public String getRole(String token) {
		return parseClaims(token).get("role", String.class);
	}
	
	public Long getExprired(String token) {
		return parseClaims(token).get("role", Long.class);
	}
	
	
	/**
	 * 
	 * getAuthentication(String token) - 인증 과정에서 사용할 Authentication 객체 생성 메서드
	 * 
	 * @param token
	 * @return
	 */
	
    public Authentication getAuthentication(String token) {
        // 1.⃣ 토큰에서 username 추출
        Long userId = getUserId(token);

        // 2. 토큰에서 Role 추출
        String role = getRole(token);
        
        System.out.println(role + " -> Role" );
        
        // 2-1. 필수 Claim이 없으면 인증 불가
        if ( userId == null || userId == 0 ||role == null || role.isEmpty()) {
        	throw new JwtException("Invalid Token: Claim is missing");
        }
        User user = userRepository.findById(userId).orElseThrow(
        			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
        		);
        
        // 3 authorities 설정
//        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // 4 Spring Security가 이해하는 UserDetails 형태로 구성
//        User user = User.builder()
//                .username(username)
//                .role(role)
//                .password("") // JWT에는 비밀번호 없음
//                .email(domainUser.getEmail())
//                .build();
        
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 5 Authentication 객체 반환
//        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
	
    
    
    
    
    
    

    
    
    /****************************************************************************
     * 
     *	Getter 대용 메소드
     *
     ****************************************************************************/
    
    /**
     * getAccessTokenExpiration() - AccessToken의 만료 시간 Getter 대용 메소드
     * 
     * 
     * @return Long accessTokenExpiredtime
     */
    public long getAccessTokenExpiration() {
    	return Long.parseLong(accessTokenExpiredTime);
    }
    
    /**
     * getRefreshTokenExpiration() - RefreshToken의 만료 시간 Getter 대용 메소드
     * 
     * 
     * @return Long accessTokenExpiredtime
     */
    public long getRefreshTokenExpiration() {
    	return Long.parseLong(refreshTokenExpiredTime);
    }
	
    
    
    
    
    
    
    
    
    
    
    /****************************************************************************
     * 
	 *	Private 메소드 - Provider 내부용
	 *	
	 ****************************************************************************/
   
    /**
     * parseClaims(String token) - Token Claims 추출 메소드
     * 
     * @param token
     * @return
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    
    /**
     * getSigningKey() - secret -> SingingKey 생성 메소드
     * 
     * @return
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	
	
	
}
