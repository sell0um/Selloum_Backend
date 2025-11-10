package com.selloum.api.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.selloum.api.auth.jwt.JwtTokenProvider;
import com.selloum.core.security.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomLogoutHandler implements LogoutHandler {
	
	private Logger LOGGER = LoggerFactory.getLogger(CustomLogoutHandler.class);

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		LOGGER.info("[ CustomLogoutHandler - logout ] : 로그아웃 시작");
		
		// 토큰 추출
		String headerToken = request.getHeader("Authorization");
		
		
		//토큰이 존재하면
		if(headerToken != null) {
			
//			String token = JwtTokenProvider;
			
		} 
		// 토큰이 존재하지 않으면 
		else {
			
		}
		
		

		
	}

}
