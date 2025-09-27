package com.selloum.api.auth.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selloum.api.auth.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author : hongseok
 * @fileName : JwtAuthenticationFilter
 * @since : 9/13/25
 * 
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager 
									, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler
									, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
		super.setAuthenticationManager(authenticationManager);
		setFilterProcessesUrl("/auth/login");
		setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
		setAuthenticationFailureHandler(customAuthenticationFailureHandler);
//		afterPropertiesSet();
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

		try {
			ObjectMapper om = new ObjectMapper();
			UserDto.request userDto = om.readValue(request.getInputStream(), UserDto.request.class);

			LOGGER.info("Login attempt : {}", userDto.getUserName());
			
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword());		
			
			setDetails(request, authRequest);
			
			return this.getAuthenticationManager().authenticate(authRequest);
			
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		
		
	}
}
