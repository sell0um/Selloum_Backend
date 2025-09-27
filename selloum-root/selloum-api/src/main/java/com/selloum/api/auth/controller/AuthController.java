package com.selloum.api.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selloum.api.auth.dto.UserDto;
import com.selloum.api.auth.service.SignupService;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.api.common.response.ResponseCode;

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
@RequestMapping("/api/auth")
public class AuthController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	private final SignupService signupService;
	
	@Operation(summary = "회원 가입", description = "전달받은 정보로 회원가입 진행")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공" , 
					content = @Content(schema = @Schema(implementation = UserDto.response.class))),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@PostMapping("/sign-up")
	public ResponseEntity<BaseResponse> signUpUser(@RequestBody UserDto.request signupRequest){
		
		LOGGER.info("[ AuthController - signUpUser ] : 회원가입 시작" + signupRequest.getUserName());
		
		String resultString = signupService.signupUser(signupRequest);
		
		BaseResponse response = BaseResponse.of(ResponseCode.REGISTER_SUCCESS, resultString);
		
		return new ResponseEntity(response, HttpStatus.valueOf(response.getStatus()));
	}

	
	

}
