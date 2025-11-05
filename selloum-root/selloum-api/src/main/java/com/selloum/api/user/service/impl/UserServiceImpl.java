package com.selloum.api.user.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.selloum.api.user.dto.UserDto;
import com.selloum.api.user.dto.UserDto.request;
import com.selloum.api.user.dto.UserDto.response;
import com.selloum.api.user.service.UserService;
import com.selloum.core.Exception.CustomException;
import com.selloum.domain.entity.User;
import com.selloum.domain.repository.UserRepository;
import com.selloum.external.mail.service.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final MailService mailService;

	
	
	@Override
	public response signup(request req) {
		
		LOGGER.info("[ UserServiceImpl - signup ] : 회원 가입" + req.getUserName());
		
		User user = User.builder()
					.name(req.getName())
					.username(req.getUserName())
					.password(passwordEncoder.encode(req.getPassword()))
					.email(req.getEmail())
					.phone(req.getPhone())
					.role("ROLE_USER")
					.status("U")
					.build();
		
		try {
			user = userRepository.save(user);
		} catch (Exception e) {
			throw new CustomException();
		}
		
		return toResponse(user);
	}





	@Override
	public boolean checkUserName(String userName) {
		return userRepository.findByUsername(userName).isPresent();
	}
	
	
	@Override
	public response getUserDetail(String username) {
		
		userRepository.findByUsername(username);
		
		return null;
	}
	



	/******************************/
	/*  UserService Util Method   */
	/******************************/
	
	
	
	public UserDto.response toResponse(User user){
		return UserDto.response.builder()
					.id(user.getId())
					.userName(user.getUsername())
					.name(user.getName())
					.password(user.getPassword())
					.email(user.getEmail())
					.phone(user.getPhone())
					.role(user.getRole())
					.build();
	}



















}
