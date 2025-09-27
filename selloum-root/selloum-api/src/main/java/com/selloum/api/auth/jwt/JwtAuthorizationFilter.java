package com.selloum.api.auth.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 토큰이 필요하지 않은 API EndPoint 관리
		
		// 토큰이 필요하지 않은 API 호출 시 다음 필터로 이동
		
		// 토큰이 필요한 API 호출 시 다음 과정을 수행
		// 1. API Request의 Header에 Authorization 속성을 확인
		String header = request.getHeader("Authorization");
		LOGGER.info("JwtAuthorizationFilter - Header 추출");
		
		
			
			// 3. 토큰의 유효 여부 확인
			// 3-1 토큰 유효 O
				//4. 토큰 기반으로 사용자 아이디 반환 받기
				//5. 사용자 아이디가 존재하는지 여부 확인
			// 3-2 토큰 유효 X
		// 1- 2
		
		
		try {
			// 1-1 Header에 토큰 존재
			if(header != null) {
				// 2. Header 내의 톹큰 추출
				String token;
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}
	

}
