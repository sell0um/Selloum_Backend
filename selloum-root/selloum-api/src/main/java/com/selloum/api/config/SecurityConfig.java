package com.selloum.api.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.selloum.api.auth.jwt.CustomAuthenticationFailureHandler;
import com.selloum.api.auth.jwt.CustomAuthenticationProvider;
import com.selloum.api.auth.jwt.CustomAuthenticationSuccessHandler;
import com.selloum.api.auth.jwt.JwtAuthenticationFilter;
import com.selloum.api.auth.service.impl.CustomUserDetailsService;

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
		
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager() 
															, authenticationSuccessHandler() 
															, authenticationFailureHandler());
		
		return http
				.csrf(AbstractHttpConfigurer::disable)															//CSRF 보호 비활성화
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))								// CORS 커스텀 설정 적용
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/**").hasRole("admin")
						.anyRequest().permitAll())																// 우선 모든 요청에 대해 접근 허용
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)			// JWT 인증 필터 추가
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 	//세션 미사용
				.formLogin(AbstractHttpConfigurer::disable)														// formLogin 비활성환
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
        configuration.setAllowedMethods(List.of("*"));      // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*"));      // 모든 헤더 허용
        configuration.setAllowCredentials(true);                // 인증 정보 허용
        configuration.setMaxAge(3600L);                         // 프리플라이트 요청 결과를 3600초 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);             // 모든 경로에 대해 이 설정 적용
        return source;
    }
    
    
    
    /***************필요한 생성자 주입을 위한 Bean 등록 메서드****************/
    
    /**
     * BCrypt 인코딩을 통하여 비밀번호에 대한 암호화를 수행합니다.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Authentication Manager 객체 생성 및 Provider 설정
     * 
     * @return AuthenticationManager
     */
    	
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(new CustomAuthenticationProvider(bCryptPasswordEncoder()));
    }
    
    /**
     * AuthenticationSuccessHandler 객체 생성
     * 
     * @return CustomAuthenticationSuccessHandler
     * 
     */
    	
    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    
    /**
     * AuthenticationFailureHandler 객체 생성
     * 
     * @return CustomAuthenticationFailureHandler
     * 
     */
    	
    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
	
}
