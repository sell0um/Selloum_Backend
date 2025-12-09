package com.selloum.api.auth.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selloum.api.common.handler.CustomAuthenticationFailureHandler;
import com.selloum.api.common.handler.CustomAuthenticationSuccessHandler;
import com.selloum.api.user.dto.UserDto;

import jakarta.annotation.PostConstruct;
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

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	@PostConstruct
	public void init() {
	    LOGGER.info("🔥 JwtAuthenticationFilter Bean initialized with URL: {}");
	}
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager 
								 , CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler
							     , CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
		super.setAuthenticationManager(authenticationManager);
		setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
		setAuthenticationFailureHandler(customAuthenticationFailureHandler);

		setFilterProcessesUrl("/auth/login");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		LOGGER.info(" [ JwtAuthenticationFilter - attemptAuthentication ] : " );


		try {
			
			UserDto.request userDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.request.class);

			LOGGER.info(" [ JwtAuthenticationFilter - attemptAuthentication ] : " + userDto.getUserName());
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.getUserName(), userDto.getPassword());		
			
			setDetails(request, authToken);
			
			return this.getAuthenticationManager().authenticate(authToken);
			
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		
		
	}
}
