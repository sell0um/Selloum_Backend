package com.selloum.api.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

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
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}
	
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		String uri = request.getRequestURI();
		
		
		// 토큰이 필요하지 않는 API 호출 발생 시 : 아래 로직 처리 없이 다음 필터로 이동
		if (WHITELIST_URLS.stream().anyMatch(uri::startsWith)) {
		    filterChain.doFilter(request, response);
		    return;
		}
		
	}
	
	

}
