package com.selloum.api.user.service;

import com.selloum.api.user.dto.EmailDto;
import com.selloum.api.user.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	
	UserDto.response signup(UserDto.request req);
	void validateUserName(String userName);
	UserDto.response getUserDetail(String username);
	UserDto.response updateUser(String userName, UserDto.request req);
	void deleteUser(String password ,HttpServletRequest req);
	UserDto.response updatePassword(String userName, UserDto.request req);
	
	
	
	String findUserName(UserDto.request req);
	
	void verifyUserIdentity(UserDto.request req);
	void resetPassword(UserDto.request req);
	
	
	

}
