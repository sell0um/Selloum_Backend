package com.selloum.api.config;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.selloum.api.auth.jwt.CustomLogoutFilter;
import com.selloum.api.auth.jwt.JwtAuthenticationFilter;
import com.selloum.api.auth.jwt.JwtAuthorizationFilter;
import com.selloum.api.auth.service.impl.CustomUserDetailsService;
import com.selloum.api.common.handler.CustomAuthenticationFailureHandler;
import com.selloum.api.common.handler.CustomAuthenticationSuccessHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;;

/**
 * 
 * @author : hongseok
 * @fileName : SecurityConfig
 * @since : 9/12/25
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtAuthorizationFilter jwtAuthorizationFilter;	// 인증 필터
	private final CustomLogoutFilter customLogoutFilter;
	private final CustomAuthenticationFailureHandler authenticationFailureHandler;
	private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
	private final AuthenticationConfiguration authenticationConfiguration;

	
	/*
	 * WebSecurityCustyomizer -> 정적 자원에 대한 보안을 적용하지 않도록 설정
	 */
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);
		    
	    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
	        authenticationManager,
	        authenticationSuccessHandler,
	        authenticationFailureHandler
	    );
	    jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
		
		return http
				.csrf(AbstractHttpConfigurer::disable)															//CSRF 보호 비활성화
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))								// CORS 커스텀 설정 적용
				.authorizeHttpRequests(auth -> auth
						
						// 회원 가입
						.requestMatchers(
								"/users/email",
								"/users/email/confirm",
								"/users/sign-up",
								"/users/check-id"
								).permitAll()
						// Swagger
					    .requestMatchers(
					            "/swagger-ui/**",
					            "/v3/api-docs/**",
					            "/v3/api-docs.yaml",
					            "/swagger-resources/**",
					            "/webjars/**"
					    		).permitAll()
					    // ADMIn 관련은 접근 제한
						.requestMatchers(
								"/admin/**"
							).hasRole("ADMIN")
						// 나머지는 인증 필요
						.anyRequest().authenticated())
				
				// 인가 필터 실행 -> 다음 인증 필터
				.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(customLogoutFilter, LogoutFilter.class)								// 로그아웃 필터
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 	//세션 미사용
				.formLogin((auth) -> auth.disable())			
				.build();
	}
	
	/**
     * CORS 설정 커스터마이징
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));      // 허용할 오리진
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*"));      // 모든 헤더 허용
        configuration.setAllowCredentials(true);                // 인증 정보 허용
        configuration.setMaxAge(3600L);                         // 프리플라이트 요청 결과를 3600초 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);             // 모든 경로에 대해 이 설정 적용
        return source;
    }
    
    
    
    /***************필요한 생성자 주입을 위한 Bean 등록 메서드****************/
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    /**
     * BCryptPasswordEncoder 객체 생성
     * 
     * @return BCryptPasswordEncoder
     * 
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	
}
