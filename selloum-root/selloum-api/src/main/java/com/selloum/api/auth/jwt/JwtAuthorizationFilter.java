package com.selloum.api.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, Object> redisTemplate;
	
	@PostConstruct
	public void init() {
	    LOGGER.info("🔥 JwtAuthorizationFilter Bean initialized with URL: {}");
	}
	
	private static final List<String> WHITELIST_URLS = Arrays.asList(
		    "/swagger-ui",
		    "/v3/api-docs",
		    "/v3/api-docs.yaml",
		    "/swagger-resources",
		    "/webjars",
		    "/favicon.ico",
		    "/auth/login",
		    "/users/sign-up",
		    "/users/email",
		    "/users/email/confirm"
    );
	
	@Value("${jwt.header.access}")
	private String accessTokenHeader;
	@Value("${jwt.header.refresh}")
	private String refreshTokenHeader;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = request.getRequestURI();
		

		LOGGER.info("[ JwtAuthorizationFilter - doFilterInternal() 호출 ]");
		
		
		// 토큰이 필요하지 않는 API 호출 발생 시 : 아래 로직 처리 없이 다음 필터로 이동
		if (WHITELIST_URLS.stream().anyMatch(uri::startsWith)) {
		    filterChain.doFilter(request, response);
		    return;
		}

		
		// 토큰이 필요한 API 호출 시 다음 과정을 수행
		

		// 1. API Request의 Header에 AccessToken을 확인
		String accessToken = request.getHeader(accessTokenHeader);		
		
		// 1-1. 접근 토큰이 없거나 Bearer 토큰이 아닐 경우
		if (accessToken == null || ! jwtTokenProvider.isStartWithPrfix(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
		
		accessToken = jwtTokenProvider.getTokenWithoutPrefix(accessToken);
		
		try {
			
			// 1-2. 접근 토큰이 존재할 경우 
				
			// 2. 접근 토큰의 유효성 체크
	    	if(jwtTokenProvider.validateToken(accessToken)) { // 접근 토큰 유효 시
				
	    		// 3. 접근 토큰 내의 사용자 정보 확인
	    		Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
	    		SecurityContextHolder.getContext().setAuthentication(auth);
	            
	    		
	            filterChain.doFilter(request, response);
	            return;
	    		// 4. 접근 토큰의 오류가 EXPIRED인지 확인
	    		
	    	} 
			
		} catch (ExpiredJwtException e){ // 토큰 만료 시
			LOGGER.info("[ JwtAuthorizationFilter - doFilterInternal() : 토큰 만료 ]");

			
		} catch (Exception e){ // 이외의 검증 요류
			LOGGER.info("[ JwtAuthorizationFilter - doFilterInternal() : 잘못된 AccessToken ]");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid access token");
            return;
		} 
		
		try {

			String username = jwtTokenProvider.getUsername(accessToken);
			String redisKey = "refresh:" + username;
			
			String refreshToken = (String)redisTemplate.opsForValue().get(redisKey);
			
			// 갱신 토큰이 없는 경우
			if(refreshToken == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Refresh token expired or not found");
                return;
			}
			
			
			if (!jwtTokenProvider.validateToken(refreshToken)) {
				redisTemplate.delete(redisKey);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Refresh token expired");
                return;
            }
			
			String role = jwtTokenProvider.getRole(refreshToken);
			String newAccessToken = jwtTokenProvider.generateToken("access",username, role);
			
			// 응답 헤더에 새 Access Token 설정
            response.setHeader("Authorization", jwtTokenProvider.getTokenWithPrefix(newAccessToken));

			
    		Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
    		SecurityContextHolder.getContext().setAuthentication(auth);
			
    		filterChain.doFilter(request, response);
			
		} catch (ExpiredJwtException  e) {	
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token expired");
		} catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token refresh failed");
		}
		
		
		
		
	}
	

}
