package com.selloum.api.user.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.selloum.api.auth.jwt.JwtTokenProvider;
import com.selloum.api.user.dto.UserDto;
import com.selloum.api.user.dto.UserDto.request;
import com.selloum.api.user.dto.UserDto.response;
import com.selloum.api.user.service.UserService;
import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;
import com.selloum.domain.entity.User;
import com.selloum.domain.repository.UserRepository;
import com.selloum.external.mail.service.MailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final MailService mailService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, Object> redisTemplate;


	
	@Value("${jwt.header.access}")
	private String accessTokenHeader;
	@Value("${jwt.header.refresh}")
	private String refreshTokenHeader;
	
	
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
			throw new CustomException(ErrorCode.USER_CREATION_FAILED);
		}
		
		return toResponse(user);
	}





	@Override
	public void validateUserName(String userName) {
	    if (userRepository.existsByUsername(userName)) {
	        throw new CustomException(ErrorCode.DUPLICATE_USER);
	    }
	}
	
	
	@Override
	public response getUserDetail(String username) {
		User user = userRepository.findByUsername(username).get();
		return toResponse(user);
	}
	

	@Override
	public void deleteUser(String password, HttpServletRequest req) {
		
		// 토큰 추출
		String accessToken = req.getHeader(accessTokenHeader);	
		accessToken = jwtTokenProvider.getTokenWithoutPrefix(accessToken);
		
		// 유저 아이디 추출
		String userName = jwtTokenProvider.getUsername(accessToken);
		
		// 해당 회원 정보 조회
		User user = userRepository.findByUsername(userName).
				orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
		// RefreshToken 제거
		String redisKey = "refresh:" + userName;
		redisTemplate.delete(redisKey);
		
		 // 5. AccessToken 블랙리스트 등록 (선택)
        long expiration = jwtTokenProvider.getExprired(accessToken);
        redisTemplate.opsForValue().set("BL:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
		
		user.userDelete();
		
		SecurityContextHolder.clearContext();
	}


	@Override
	public response updateUser(String userName, UserDto.request req) {
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
		user.updateInfo(req.getName(), req.getUserName(), req.getEmail(), req.getPhone());

		return toResponse(user);
	}


	@Override
	public String findUserName(UserDto.request req) {
		User user = userRepository.findByEmail(req.getEmail()).orElseThrow(
				() -> new CustomException(ErrorCode.USER_NOT_FOUND)
				);

		if(!user.getName().equals( req.getName() )) {
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		};
		
		return user.getName();
	}





	@Override
	public response updatePassword(String userName, UserDto.request req) {
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
		user.changePassword(passwordEncoder.encode(req.getPassword()));
		
		return toResponse(user);
	}

	@Override
	public void verifyUserIdentity(UserDto.request req) {
		
		User user = userRepository.findByUsernameAndEmailAndName(
				req.getUserName(), req.getEmail(), req.getName()
				).orElseThrow( () -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
		if(user.getStatus() != "U") {
			throw new CustomException(ErrorCode.INACTIVE_USER);
		}
		
		
	}



	@Override
	public void resetPassword(UserDto.request req) {
		
		User user = userRepository.findByEmail(req.getEmail())
				.orElseThrow(
						() -> new CustomException(ErrorCode.USER_NOT_FOUND)
				);
		
		user.changePassword(passwordEncoder.encode(req.getPassword()));
		
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
