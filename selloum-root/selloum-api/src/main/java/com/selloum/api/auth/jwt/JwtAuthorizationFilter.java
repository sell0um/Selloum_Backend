package com.selloum.api.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;

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
	private final RedisTokenUtils redisTokenUtils;
	
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
		    "/users/check-id",
		    "/users/email",
		    "/users/email/confirm"
    );
	
	@Value("${jwt.header.access}")
	private String accessTokenHeader;
	@Value("${jwt.header.refresh}")
	private String refreshTokenHeader;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    String uri = request.getRequestURI();
	    LOGGER.info("[ JwtAuthorizationFilter - doFilterInternal() 호출 : {} ]", uri);

	    // 1️ 화이트리스트
	    if (WHITELIST_URLS.stream().anyMatch(uri::startsWith)) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    // 2️ 헤더에서 AccessToken 추출
	    String accessToken = request.getHeader(accessTokenHeader);

	    if (accessToken == null || !jwtTokenProvider.isStartWithPrfix(accessToken)) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    // 접두사 제거한 순수 토큰 추출
	    accessToken = jwtTokenProvider.getTokenWithoutPrefix(accessToken);

	    try {
	        LOGGER.info("[ JwtAuthorizationFilter - AccessToken 존재 확인 ]");

	        // 유효한 AccessToken인 경우
	        if (jwtTokenProvider.validateToken(accessToken)) {

	            // 블랙리스트 확인
	            if (redisTokenUtils.isBlacklisted(accessToken)) {
	                writeErrorResponse(response, ErrorCode.INVALID_TOKEN);
	                return;
	            }

	            // 정상 인증
	            Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
	            
	            if (auth != null) {
	                LOGGER.info("✅ AccessToken don't have Authentication {}", auth.getPrincipal());
	            }
	            
	            SecurityContextHolder.getContext().setAuthentication(auth);

	            filterChain.doFilter(request, response);
	            return;
	        }

	    } catch (ExpiredJwtException e) {
	        LOGGER.info("[ JwtAuthorizationFilter - AccessToken 만료 ]");
	        // 아래 Refresh Token 로직으로 진행
	    } catch (Exception e) {
	        LOGGER.error("[ JwtAuthorizationFilter - AccessToken 검증 오류 ]", e);
	        writeErrorResponse(response, ErrorCode.INVALID_TOKEN);
	        return;
	    }

	    // 3️⃣ Refresh Token 검증
	    try {
	        String username = jwtTokenProvider.getUsername(accessToken);
	        String refreshToken = redisTokenUtils.getRefreshToken(username);

	        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
	            redisTokenUtils.deleteRefreshToken(username);
	            writeErrorResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRED);
	            return;
	        }

	        String role = jwtTokenProvider.getRole(refreshToken);
	        String newAccessToken = jwtTokenProvider.generateToken("access", username, role);

	        response.setHeader("Authorization", jwtTokenProvider.getTokenWithPrefix(newAccessToken));

	        Authentication auth = jwtTokenProvider.getAuthentication(newAccessToken);
	        LOGGER.info("✅ AUTH CHECK: {}", auth);
	        if (auth != null) {
	            LOGGER.info("✅ AUTH PRINCIPAL: {}", auth.getPrincipal());
	            LOGGER.info("✅ AUTH AUTHORITIES: {}", auth.getAuthorities());
	        }
	        SecurityContextHolder.getContext().setAuthentication(auth);
	        
	        
	        LOGGER.info("🎯 [JwtAuthorizationFilter] SecurityContext Authentication : {}", 
	                SecurityContextHolder.getContext().getAuthentication());

	        filterChain.doFilter(request, response);

	    } catch (Exception e) {
	        LOGGER.error("[ JwtAuthorizationFilter - RefreshToken 처리 실패 ]", e);
	        writeErrorResponse(response, ErrorCode.REFRESH_TOKEN_EXPIRED);
	    }
	}
	
	

	/**
	 * 에러 응답 작성 유틸 - 커밋 방지 및 중복 호출 방지
	 */
	private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
	    if (response.isCommitted()) {
	        LOGGER.warn("[ JwtAuthorizationFilter - 이미 커밋된 응답, writeErrorResponse 생략 ]");
	        return;
	    }

	    response.resetBuffer(); // 혹시 기존 버퍼 남아있을 경우 초기화
	    response.setStatus(errorCode.getStatus().getCode());
	    response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().write(
	            String.format("{\"code\":\"%s\",\"message\":\"%s\"}", errorCode.getCode(), errorCode.getMessage())
	    );
	    response.flushBuffer(); // 즉시 커밋하고 필터 종료
	}
	

}
