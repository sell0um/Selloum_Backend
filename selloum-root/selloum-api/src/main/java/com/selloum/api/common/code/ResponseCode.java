package com.selloum.api.common.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	// 회원가입 및 Auth
	SUCCESS(HttpStatus.OK,"S-M000","요청이 성공적으로 처리되었습니다."),
	REGISTER_SUCCESS(HttpStatus.OK, "S-M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "S-M002", "로그인 되었습니다."),
    REISSUE_SUCCESS(HttpStatus.OK, "S-M003", "재발급 되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "S-M004", "로그아웃 되었습니다."),
    DELETED_USER(HttpStatus.FORBIDDEN, "S-M004", "탈퇴 처리된 계정입니다."),
	
	// email 인증 관련
	EMAIL_SEND_SUCCESS(HttpStatus.OK, "S-E001", "인증 이메일 발송에 성공했습니다."),
	EMAIL_VERIFY_SUCCESS(HttpStatus.OK, "S-E001", "이메일 인증에 성공하였습니다.");
	
	
    private final HttpStatus status;
    private final String code;
    private final String message;
}
