package com.selloum.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
		
	// 401 - Unauthorized (인증되지 않음)
	// 403 - Forbidden (권한 없음)
	// 409 - Conflict (요청이 서버 리소스와 맞지 않음)
	
    /* ==============================
	    AUTH (E-Axxx)
	    ============================== */
	 LOGIN_FAIL(Status.FORBIDDEN, "E-A000", "로그인에 실패하였습니다."),
	 INVALID_TOKEN(Status.UNAUTHORIZED, "E-A001", "유효하지 않은 토큰입니다."),
	 EXPIRED_TOKEN(Status.UNAUTHORIZED, "E-A002", "토큰이 만료되었습니다."),
	 ACCESS_DENIED(Status.FORBIDDEN, "E-A003", "접근이 거부되었습니다."),
	 REFRESH_TOKEN_EXPIRED(Status.UNAUTHORIZED, "E-A004", "리프레시 토큰이 만료되었습니다."),
	 TOKEN_MISSING(Status.UNAUTHORIZED, "E-A005", "토큰이 존재하지 않습니다."),
	 INVALID_CREDENTIAL(Status.BAD_REQUEST, "E-A006", "잘못된 인증 정보입니다."),
	 UNSUPPORTED_AUTH_METHOD(Status.BAD_REQUEST, "E-A007", "지원되지 않는 인증 방식입니다."),
	
	 /* ==============================
	    USER (E-Uxxx)
	    ============================== */
	 USER_NOT_FOUND(Status.NOT_FOUND, "E-U001", "사용자를 찾을 수 없습니다."),
	 DUPLICATE_USER(Status.BAD_REQUEST, "E-U002", "이미 존재하는 사용자입니다."),
	 INVALID_USER_INFO(Status.BAD_REQUEST, "E-U003", "사용자 정보가 올바르지 않습니다."),
	 PASSWORD_MISMATCH(Status.BAD_REQUEST, "E-U004", "비밀번호가 일치하지 않습니다."),
	 UNAUTHORIZED_USER(Status.UNAUTHORIZED, "E-U005", "인증되지 않은 사용자입니다."),
	 DELETED_USER(Status.BAD_REQUEST, "E-U006", "탈퇴한 사용자입니다."),
	 INACTIVE_USER(Status.FORBIDDEN, "E-U007", "비활성화된 사용자입니다."),
	 USER_CREATION_FAILED(Status.INTERNAL_SERVER_ERROR, "E-U008", "사용자 생성 중 오류가 발생했습니다."),
	
	 /* ==============================
	    NOTICE (E-Nxxx)
	    ============================== */
	 NOTICE_NOT_FOUND(Status.NOT_FOUND, "E-N001", "공지사항을 찾을 수 없습니다."),
	 NOTICE_CREATE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-N002", "공지사항 등록 중 오류가 발생했습니다."),
	 NOTICE_UPDATE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-N003", "공지사항 수정 중 오류가 발생했습니다."),
	 NOTICE_DELETE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-N004", "공지사항 삭제 중 오류가 발생했습니다."),
	 INVALID_NOTICE_PARAM(Status.BAD_REQUEST, "E-N005", "공지사항 요청 파라미터가 올바르지 않습니다."),
	
	 /* ==============================
	    DIARY (E-Dxxx)
	    ============================== */
	 DIARY_NOT_FOUND(Status.NOT_FOUND, "E-D001", "다이어리를 찾을 수 없습니다."),
	 DIARY_CREATE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-D002", "다이어리 작성 중 오류가 발생했습니다."),
	 DIARY_UPDATE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-D003", "다이어리 수정 중 오류가 발생했습니다."),
	 DIARY_DELETE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-D004", "다이어리 삭제 중 오류가 발생했습니다."),
	 INVALID_EMOTION_TYPE(Status.BAD_REQUEST, "E-D005", "유효하지 않은 감정 유형입니다."),
	 DIARY_ACCESS_DENIED(Status.FORBIDDEN, "E-D006", "해당 다이어리에 접근할 수 없습니다."),
	 EMPTY_DIARY_CONTENT(Status.BAD_REQUEST, "E-D007", "다이어리 내용이 비어 있습니다."),
	
	 /* ==============================
	    REPORT (E-Rxxx)
	    ============================== */
	 REPORT_NOT_FOUND(Status.NOT_FOUND, "E-R001", "리포트를 찾을 수 없습니다."),
	 REPORT_CREATE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-R002", "리포트 생성 중 오류가 발생했습니다."),
	 REPORT_UPDATE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-R003", "리포트 수정 중 오류가 발생했습니다."),
	 REPORT_DELETE_FAILED(Status.INTERNAL_SERVER_ERROR, "E-R004", "리포트 삭제 중 오류가 발생했습니다."),
	 INVALID_REPORT_TYPE(Status.BAD_REQUEST, "E-R005", "유효하지 않은 리포트 유형입니다."),
	 REPORT_ACCESS_DENIED(Status.FORBIDDEN, "E-R006", "해당 리포트에 접근할 수 없습니다."),
	
	 /* ==============================
	    COMMON (E-Cxxx)
	    ============================== */
	 INVALID_REQUEST(Status.BAD_REQUEST, "E-C001", "잘못된 요청입니다."),
	 VALIDATION_FAILED(Status.BAD_REQUEST, "E-C002", "입력값 검증에 실패했습니다."),
	 DATA_NOT_FOUND(Status.NOT_FOUND, "E-C003", "요청한 데이터를 찾을 수 없습니다."),
	 METHOD_NOT_ALLOWED(Status.BAD_REQUEST, "E-C004", "허용되지 않은 요청 방식입니다."),
	 INTERNAL_ERROR(Status.INTERNAL_SERVER_ERROR, "E-C005", "서버 내부 오류가 발생했습니다."),
	 DATABASE_ERROR(Status.INTERNAL_SERVER_ERROR, "E-C006", "데이터베이스 처리 중 오류가 발생했습니다."),
	 EXTERNAL_API_ERROR(Status.BAD_REQUEST, "E-C007", "외부 API 호출 중 오류가 발생했습니다.");
	
	
    private final Status status;
    private final String code;
    private final String message;

}
