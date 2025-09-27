package com.selloum.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	REGISTER_SUCCESS(200, "S-M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(200, "S-M002", "로그인 되었습니다."),
    REISSUE_SUCCESS(200, "S-M003", "재발급 되었습니다."),
    LOGOUT_SUCCESS(200, "S-M004", "로그아웃 되었습니다.");
	
    private final int status;
    private final String code;
    private final String message;
}
