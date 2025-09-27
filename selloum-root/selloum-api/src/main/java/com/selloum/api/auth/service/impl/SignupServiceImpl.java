package com.selloum.api.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.selloum.api.auth.dto.UserDto;
import com.selloum.api.auth.service.SignupService;
import com.selloum.domain.entity.User;
import com.selloum.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SignupServiceImpl implements SignupService {

	private final Logger LOGGER = LoggerFactory.getLogger(SignupServiceImpl.class);
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bcyBCryptPasswordEncoder;
	
	@Override
	public String signupUser(UserDto.request request) {
		
		LOGGER.info("[ SignupServiceImpl - signupUser ] : 회원가입 시작 - " + request.getUserName());
		
		// 계정 존재 여부 확인
		User user = userRepository.findByUsername(request.getUserName()).get();
		
		// 비밀번호 인코딩
		request.setPassword(bcyBCryptPasswordEncoder.encode(request.getPassword()));
		user = userRepository.save(user);
		
		return user.getUsername();
	}

}
