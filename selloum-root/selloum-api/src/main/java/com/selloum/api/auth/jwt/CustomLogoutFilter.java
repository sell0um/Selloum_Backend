package com.selloum.api.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.user.dto.UserDto.request;
import com.selloum.core.code.ErrorCode;
import com.selloum.core.security.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean{
	
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtUtils jwtUtils;	
	private final RedisTokenUtils redisTokenUtils;
	
	@Value("${jwt.header.access}")
	private String accessTokenHeader;
	@Value("${jwt.prefix}")
	private String prefix;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}
	
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
        if (!request.getRequestURI().equals("/auth/logout") ||
                !request.getMethod().equalsIgnoreCase("POST")) {
                filterChain.doFilter(request, response);
                return;
        }
            
		
		String accessToken = request.getHeader(accessTokenHeader);
		
		if(accessToken == null || Strings.isBlank(accessToken) ) { // 토큰 미존재 시
			writeErrorResponse(response, ErrorCode.TOKEN_MISSING);
		}
		
		accessToken = jwtTokenProvider.getTokenWithoutPrefix(accessToken);
		String userName = jwtTokenProvider.getUsername(accessToken);
		
		
		redisTokenUtils.deleteRefreshToken(userName);
		
		long expiration = jwtTokenProvider.getAccessTokenExpiration();
		redisTokenUtils.addToBlacklist(accessToken, expiration);
		
		
		ResponseCode code = ResponseCode.LOGOUT_SUCCESS;
        response.setStatus(code.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(
        		String.format("{\"code\":%s,\"message\":\"%s\"}", code.getCode(), code.getMessage())
        		);
		
		
		
	}
	
    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().getCode());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"code\":%s,\"message\":\"%s\"}", errorCode.getCode(), errorCode.getMessage())
        );
    }
	
	

}
