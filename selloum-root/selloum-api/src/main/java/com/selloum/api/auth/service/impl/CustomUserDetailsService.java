package com.selloum.api.auth.service.impl;

import java.nio.file.AccessDeniedException;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.selloum.api.auth.domain.CustomUserDetails;
import com.selloum.domain.entity.User;
import com.selloum.domain.enums.UserStatus;
import com.selloum.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		if(userName == null || userName.isEmpty()) {
			throw new AuthenticationServiceException("입력된 ID가 올바르지 않습니다.");
		}
		
		User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userName));
		
		if(!user.getStatus().equals(UserStatus.U)) {
			throw new AuthenticationServiceException("접근이 제한된 사용자입니다 : " + user.getStatus().getDescription());
		}

		
		return new CustomUserDetails(user);
		
		
		
	}

}
