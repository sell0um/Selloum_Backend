package com.selloum.api.common.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selloum.api.common.code.ErrorCode;
import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.common.response.BaseResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{

	private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

        LOGGER.warn("[ CustomAuthenticationFailureHandler - onAuthenticationFailure( )] 로그인 실패 - {}", exception.getMessage());

        // 기본 응답 상태
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorCode code = ErrorCode.LOGIN_FAIL;

        // 예외 타입별 상세 처리
        if (exception instanceof BadCredentialsException) {
            code = ErrorCode.BAD_CREDENTIALS; // 비밀번호 불일치
        } else if (exception instanceof UsernameNotFoundException) {
            code = ErrorCode.USER_NOT_FOUND; // 존재하지 않는 사용자
        } else if (exception instanceof LockedException) {
            code = ErrorCode.USER_LOCKED; // 계정 잠김
        } else if (exception instanceof DisabledException) {
            code = ErrorCode.USER_DISABLED; // 비활성화된 계정
        } else if (exception instanceof AccountExpiredException) {
            code = ErrorCode.USER_EXPIRED; // 계정 만료
        } else if (exception instanceof CredentialsExpiredException) {
            code = ErrorCode.CREDENTIALS_EXPIRED; // 비밀번호 만료
        }

        // 응답 생성
        BaseResponse<Object> responseBody = BaseResponse.of(code, null);

        // JSON 직렬화 후 응답
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(responseBody));

        LOGGER.info("[ CustomAuthenticationFailureHandler - onAuthenticationFailure( )] → 응답 전송 완료: {}", code.name());
		
		
		
	}

	

}
