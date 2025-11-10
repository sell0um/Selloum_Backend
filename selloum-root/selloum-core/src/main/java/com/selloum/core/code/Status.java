package com.selloum.core.code;

public enum Status {
	
    OK(200),
    BAD_REQUEST(400), 				// 잘못된 요청
    UNAUTHORIZED(401),				// 인증되지 않음
    FORBIDDEN(403),					// 권한 없음
    NOT_FOUND(404),					// 자원이 존재하지 않음
    CONFLICT(409),					// 요청과 서버의 상태가 충돌
    UNPROCESSABLE_ENTITY(422),		// 요청된 지시를 따를 수 없음
    INTERNAL_SERVER_ERROR(500);		// 서버 내부 문제 발생
	
	
	private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
