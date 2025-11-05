package com.selloum.api.common.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	
	// 401 - Unauthorized (인증되지 않음)
	// 403 - Forbidden (권한 없음)
	// 409 - Conflict (요청이 서버 리소스와 맞지 않음)
	LOGIN_FAIL(HttpStatus.FORBIDDEN,"E-A000","로그인에 실패하였습니다."),

	CREDENTIALS_EXPIRED(HttpStatus.UNAUTHORIZED, "E-A001", "비밀번호가 만료되었습니다."),
	DELETED_USER(HttpStatus.UNAUTHORIZED, "E-A002", "탈퇴된 계정 입니다"),
	BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "E-A003", "비밀번호가 일치하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "E-A004", "존재하지 않는 사용자입니다."),
	USER_LOCKED(HttpStatus.UNAUTHORIZED, "E-A005", "계정이 잠겼습니다."),
	USER_DISABLED(HttpStatus.UNAUTHORIZED, "E-A006", "비활성화된 계정입니다."),
	USER_EXPIRED(HttpStatus.UNAUTHORIZED, "E-A007", "계정이 만료되었습니다."),
	
	

	DUPLICATE_EMAIL(HttpStatus.CONFLICT,"E-U001","이미 존재하는 이메일입니다."),
	DUPLICATE_USERNAME(HttpStatus.CONFLICT, "E-U002", "이미 존재하는 아이디 입니다"),
	;
	
	
	
	
	
	
	
	
	
	
	
    private final HttpStatus status;
    private final String code;
    private final String message;

}
