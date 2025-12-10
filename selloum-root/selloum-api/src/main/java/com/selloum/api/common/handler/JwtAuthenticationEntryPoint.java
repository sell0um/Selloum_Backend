package com.selloum.api.common.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.core.code.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        // 기본적으로 "인증 실패" 상황을 처리
        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
        
        // 추가 정보가 있다면 request attribute로 전달 가능
        // ex) JwtAuthorizationFilter에서 예외 종류 설정
        if (request.getAttribute("exception") != null) {
            String exceptionType = (String) request.getAttribute("exception");

            if ("INVALID_TOKEN".equals(exceptionType)) {
                errorCode = ErrorCode.INVALID_TOKEN;
            } else if ("EXPIRED_TOKEN".equals(exceptionType)) {
                errorCode = ErrorCode.EXPIRED_TOKEN;
            }
        }

        BaseResponse<?> body = BaseResponse.of(errorCode);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}