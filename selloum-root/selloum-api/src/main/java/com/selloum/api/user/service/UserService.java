package com.selloum.api.user.service;

import com.selloum.api.user.dto.EmailDto;
import com.selloum.api.user.dto.UserDto;

public interface UserService {
	
	UserDto.response signup(UserDto.request req);
	boolean checkUserName(String userName);
	UserDto.response getUserDetail(String username);
	
	
	

}
