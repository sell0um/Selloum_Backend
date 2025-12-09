package com.selloum.api.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selloum.api.auth.domain.CustomUserDetails;
import com.selloum.api.auth.jwt.RedisTokenUtils;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "User 관련 API")
@RequestMapping("/users")
public class UserController {
	
	private final RedisTokenUtils redisTokenUtils;
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
		
		userService.validateUserName(req.getUserName());
		return ResponseUtil.success(ResponseCode.AVAILABLE_USERNAME,"NotExsists");
		
	}
	
	
	
	/**
	 *
	 * mailSend() - 인증 이메일 발송 요청
	 * 
	 * @param req
	 * @return
	 */
	@Operation(summary = "이메일 인증 코드 발송 요청", description = "회원 가입 시 등록된 이메일로 인증 코드 발송 요청")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@PostMapping("/email")
	public ResponseEntity<BaseResponse<String>> sendEmail(@Valid @RequestBody EmailDto.sendRequest req){
		
		mailService.sendEmail(req.getEmail());
		return ResponseUtil.success(ResponseCode.EMAIL_SEND_SUCCESS, null);
		
	}
	
	
	/**
	 * emailConfirm() - 이메일 인증 확인
	 * 
	 * @param req
	 * @return
	 */
	@Operation(summary = "이메일 인증 코드 검증", description = "회원 가입 시 이메일 인증 코드 동일 여부 확인")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@PostMapping("/email/confirm")
	public ResponseEntity<BaseResponse<EmailDto.response>> verifyEmail(@Valid @RequestBody EmailDto.verifyRequest req){
		
		
		boolean isValid = mailService.isVerifiedEmail(req.getEmail(), req.getVerifyCode());
		EmailDto.response response = EmailDto.response.builder()
						.verifyCode(req.getVerifyCode())
						.result(isValid)
						.build();
		
		return ResponseUtil.success(ResponseCode.EMAIL_VERIFY_SUCCESS,response);
		
	}
	
	
	/**
	 * 
	 * getUserDetail() - 회원 상세 정보
	 * 
	 * @param req
	 * @return
	 */
	
	@Operation(summary = "회원 정보 조회", description = "회원 정보 조회")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	
	@GetMapping("/me")
	public ResponseEntity<BaseResponse<UserDto.response>> getUserDetail(@AuthenticationPrincipal CustomUserDetails userDetails){
		UserDto.response response = userService.getUserDetail(userDetails.getUser().getUsername());
		return ResponseUtil.success(ResponseCode.USER_FOUND,response);
		
	}
	
	/**
	 * 
	 * updateUserDetail() - 회원 정보 수정
	 * 
	 * @param req
	 * @return
	 */
	
	@PutMapping("/me")
	public ResponseEntity<BaseResponse<UserDto.response>> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails
																	, @Valid @RequestBody UserDto.request req){
		
		UserDto.response response = userService.updateUser(userDetails.getUser().getUsername(), req);
		return ResponseUtil.success(ResponseCode.USER_UPDATED,response);
		
	}
	
	/**
	 * 
	 * deleteUserDetail() - 회원 탈퇴
	 * 
	 * @param req
	 * @return
	 */
	
	@DeleteMapping("/me")
	public ResponseEntity<BaseResponse<String>> deleteUser(@Valid @RequestBody UserDto.request req , HttpServletRequest servletRequest){
		userService.deleteUser(req.getPassword(), servletRequest);
		return ResponseUtil.success(ResponseCode.DELETED_USER,null);
	}
	
	/**
	 * 
	 * updatePassword() - 비밀번호 변경
	 * 
	 * @param req
	 * @return
	 */
	
	@PutMapping("/me/password")
	public ResponseEntity<BaseResponse<String>> updatePassword( @AuthenticationPrincipal CustomUserDetails userDetails,
																@Valid @RequestBody UserDto.request req){
		
		userService.updatePassword(userDetails.getUser().getUsername(), req);
		
		return ResponseUtil.success(ResponseCode.PASSWORD_CHANGED,null);
		
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
		
		String userName = userService.findUserName(req);
		
		return ResponseUtil.success(ResponseCode.USER_FOUND,userName);
		
	}
	
	/**
	 * 
	 * findPassword() - 비밀번호 조회
	 * 
	 * @param req
	 * @return
	 */
	
	@PostMapping("/reset-pwd/request")
	public ResponseEntity<BaseResponse<String>> verifyUserIdentity(@Valid @RequestBody UserDto.request req){
		
		userService.verifyUserIdentity(req);
		mailService.sendResetCodeEmail(req.getEmail());
		
		return ResponseUtil.success(ResponseCode.EMAIL_SEND_SUCCESS, null);
		
	}
	
	@PostMapping("/reset-pwd/verify")
	public ResponseEntity<BaseResponse<EmailDto.response>> verifyResetEmailCode(@Valid @RequestBody EmailDto.verifyRequest req){
		
		boolean isValid = mailService.isverifiedResetCodeEmail(req.getEmail(), req.getVerifyCode());
		EmailDto.response response = EmailDto.response.builder()
						.verifyCode(req.getVerifyCode())
						.result(isValid)
						.build();
		
		return ResponseUtil.success(ResponseCode.EMAIL_VERIFY_SUCCESS,response);
		
	}
	
	
	/**
	 * 
	 * findPassword() - 비밀번호 조회
	 * 
	 * @param req
	 * @return
	 */
	@PutMapping("/reset-pwd")
	public ResponseEntity<BaseResponse<String>> resetPassword(@Valid @RequestBody UserDto.request req){
		
		if(redisTokenUtils.isVerifiedResetCodeEmail(req.getEmail())) { // 인증된 이메일 + 5분 이내 재설정이면
			userService.resetPassword(req);
		}
		
		return ResponseUtil.success(ResponseCode.PASSWORD_CHANGED,null);
		
	}
	
	
	

}
