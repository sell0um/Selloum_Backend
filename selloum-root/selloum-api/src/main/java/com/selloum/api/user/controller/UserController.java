package com.selloum.api.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.api.common.response.ResponseUtil;
import com.selloum.api.user.dto.EmailDto;
import com.selloum.api.user.dto.UserDto;
import com.selloum.api.user.service.UserService;
import com.selloum.external.mail.service.MailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "User 관련 API")
@RequestMapping("/users")
public class UserController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final MailService mailService;
	
	
	/**
	 * signup() - 회원가입 컨트롤러
	 * 
	 * @param UserDto.reqeust
	 * @return ResponseEntity<BaseResponse<UserDto.response>>
	 * 
	 */
	
	@Operation(summary = "회원가입", description = "회원 가입 API")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@PostMapping("/sign-up")
	public ResponseEntity<BaseResponse<UserDto.response>> signUp(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ AuthController - signUp ] : 회원가입 시작" + req.getUserName());
		UserDto.response response = userService.signup(req);
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS, response);
		
	}
	
	
	/**
	 * 
	 * checkUserName() - 아이디 중복 검사
	 * 
	 * @param UserDto.request req
	 * @return ResponseEntity<BaseResponse<String>>
	 */
	
	@Operation(summary = "아이디 중복 검사", description = "회원 가입 시 아이디 중복 여부 확인")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@GetMapping("/check-id")
	public ResponseEntity<BaseResponse<String>> checkUserName(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - checkUserName ] : 아이디 중복 확인" + req.getUserName());
		boolean isExists = userService.checkUserName(req.getUserName());
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,
				isExists ? "EXISTS": "Not EXISTS" );
		
	}
	
	
	
	/**
	 *
	 * mailSend() - 인증 이메일 발송 요청
	 * 
	 * @param req
	 * @return
	 */
	
	@PostMapping("/email")
	public ResponseEntity<BaseResponse<String>> sendEmail(@Valid @RequestBody EmailDto.sendRequest req){
		
		LOGGER.info("[ UserController - sendEmail ] : 이메일 인증 요청" + req.getEmail());
		mailService.sendEmail(req.getEmail());
		return ResponseUtil.success(ResponseCode.EMAIL_SEND_SUCCESS, null);
		
	}
	
	
	/**
	 * emailConfirm() - 이메일 인증 확인
	 * 
	 * @param req
	 * @return
	 */
	
	@PostMapping("/email/confirm")
	public ResponseEntity<BaseResponse<EmailDto.response>> verifyEmail(@Valid @RequestBody EmailDto.verifyRequest req){
		
		LOGGER.info("[ UserController - verifyEmail ] : 이메일 인증 확인" + req.getEmail());
		
		boolean isValid = mailService.verifyEmail(req.getEmail(), req.getVerifyCode());
		EmailDto.response response = EmailDto.response.builder()
						.verifyCode(req.getVerifyCode())
						.result(isValid)
						.build();
		
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,response);
		
	}
	
	
	/**
	 * 
	 * getUserDetail() - 회원 상세 정보
	 * 
	 * @param req
	 * @return
	 */
	
	@GetMapping("/me")
	public ResponseEntity<BaseResponse<UserDto.response>> getUserDetail(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - checkUserName ] : 아이디 중복 확인" + req.getUserName());
		UserDto.response response = userService.getUserDetail(req.getUserName());
		return ResponseUtil.success(ResponseCode.EMAIL_VERIFY_SUCCESS,response);
		
	}
	
	/**
	 * 
	 * updateUserDetail() - 회원 상세 수정
	 * 
	 * @param req
	 * @return
	 */
	
	@PutMapping("/me")
	public ResponseEntity<BaseResponse<UserDto.response>> updateUserDetail(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - checkUserName ] : 아이디 중복 확인" + req.getUserName());
		
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,null);
		
	}
	
	/**
	 * 
	 * deleteUserDetail() - 회원 탈퇴
	 * 
	 * @param req
	 * @return
	 */
	
	@DeleteMapping("/me")
	public ResponseEntity<BaseResponse<String>> deleteUserDetail(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - deleteUserDetail ] : 회원 탈퇴" + req.getUserName());
		
		
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,null);
		
	}
	
	/**
	 * 
	 * updatePassword() - 비밀번호 변경
	 * 
	 * @param req
	 * @return
	 */
	
	@PutMapping("/me/password")
	public ResponseEntity<BaseResponse<String>> updatePassword(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - updatePassword ] : 비밀번호 변경" + req.getUserName());
		
		
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,null);
		
	}
	
	/**
	 * 
	 * findUserName() - 아이디 조회
	 * 
	 * @param req
	 * @return
	 */
	
	@PostMapping("/find-id")
	public ResponseEntity<BaseResponse<String>> findUserName(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - findUserName ] : 아이디 찾기" + req.getUserName());

		
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,null);
		
	}
	
	/**
	 * 
	 * findPassword() - 비밀번호 조회
	 * 
	 * @param req
	 * @return
	 */
	
	@PostMapping("/find-pwd")
	public ResponseEntity<BaseResponse<String>> findPassword(@Valid @RequestBody UserDto.request req){
		
		LOGGER.info("[ UserController - findPassword ] : 비밀번호 찾기" + req.getUserName());
		
		
		return ResponseUtil.success(ResponseCode.REGISTER_SUCCESS,null);
		
	}
	
	
	

}
