package com.selloum.api.auth.service;

import com.selloum.api.auth.dto.UserDto;

public interface SignupService {
	
	String signupUser(UserDto.request request);
	
	
}
