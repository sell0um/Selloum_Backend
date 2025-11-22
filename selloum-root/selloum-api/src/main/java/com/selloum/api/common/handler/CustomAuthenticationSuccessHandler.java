package com.selloum.api.common.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.selloum.api.auth.domain.CustomUserDetails;
import com.selloum.api.auth.jwt.JwtTokenProvider;
import com.selloum.api.auth.jwt.RedisTokenUtils;
import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.core.code.ErrorCode;
import com.selloum.domain.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final RedisTokenUtils redisTokenUtils;

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		LOGGER.info(" [ CustomAuthenticationSuccessHandler - onAuthenticationSuccess() ] - 로그인 성공 처리 핸들러");
		

        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        BaseResponse<User> responseBody = null;
        
        if(user.getStatus().equals("D")) { // 회원 상태가 D(delete)라면
        	response.setStatus(HttpStatus.FORBIDDEN.value());
            writeJsonResponse(response, BaseResponse.of(ErrorCode.DELETED_USER));
            return;
        	
        }
        	
    	// 권한 정리
    	String role = user.getRole();
    	
    	// 토큰 생성
    	String accessToken = jwtTokenProvider.generateToken("access",user.getUsername(), role);
    	String refreshToken = jwtTokenProvider.generateToken("refresh",user.getUsername(), role);
    	
    	// refreshToken DB(Redis)에 저장
    	long refreshExp = jwtTokenProvider.getRefreshTokenExpiration();
        String redisKey = "refresh:" + user.getUsername();
        redisTokenUtils.saveRefreshToken(redisKey, refreshToken, refreshExp);
    	
        
    	response.setHeader("Authorization", jwtTokenProvider.getTokenWithPrefix(accessToken));
	    responseBody = BaseResponse.of(ResponseCode.LOGIN_SUCCESS, user);
    	response.setStatus(HttpStatus.OK.value());
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        writeJsonResponse(response, responseBody);
		
	}
	
    private void writeJsonResponse(HttpServletResponse response, Object body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

}
