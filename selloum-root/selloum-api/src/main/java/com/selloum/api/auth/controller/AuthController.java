package com.selloum.api.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selloum.api.auth.service.AuthService;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.api.user.dto.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth API", description = "Authentication 관련 API")
@RequestMapping("/auth")
public class AuthController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	private final AuthService authService;
	
	@Operation(summary = "LOGOUT", description = "로그아웃")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공" , 
					content = @Content(schema = @Schema(implementation = UserDto.response.class))),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@PostMapping("/logout")
	public ResponseEntity<BaseResponse> signUpUser(@RequestBody UserDto.request signupRequest){
		
		return null;

	}

	
	

}
