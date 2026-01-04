package com.selloum.api.common.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
	
	// 회원가입 및 Auth
	/* ==============================
	    AUTH (S-Axxx)
	    ============================== */
	 LOGIN_SUCCESS(HttpStatus.OK, "S-A001", "로그인에 성공하였습니다."),
	 LOGOUT_SUCCESS(HttpStatus.OK, "S-A002", "로그아웃이 완료되었습니다."),
	 TOKEN_REISSUED(HttpStatus.OK, "S-A003", "액세스 토큰이 재발급되었습니다."),
	 REGISTER_SUCCESS(HttpStatus.CREATED, "S-A004", "회원가입이 완료되었습니다."),
	
	 /* ==============================
	    USER (S-Uxxx)
	    ============================== */
	 USER_FOUND(HttpStatus.OK, "S-U001", "사용자 정보를 성공적으로 조회하였습니다."),
	 USER_UPDATED(HttpStatus.OK, "S-U002", "사용자 정보가 성공적으로 수정되었습니다."),
	 USER_DELETED(HttpStatus.OK, "S-U003", "사용자 계정이 성공적으로 삭제되었습니다."),
	 PASSWORD_CHANGED(HttpStatus.OK, "S-U004", "비밀번호가 성공적으로 변경되었습니다."),
	 USER_CREATED(HttpStatus.CREATED, "S-U005", "새로운 사용자가 등록되었습니다."),	
     DELETED_USER(HttpStatus.FORBIDDEN, "S-U006", "탈퇴 처리 되었습니다."),
     AVAILABLE_USERNAME(HttpStatus.OK, "S-U007", "사용 가능한 아이디입니다."),
		
	 
	 /* ==============================
	    NOTICE (S-Nxxx)
	    ============================== */
	 NOTICE_LIST_FOUND(HttpStatus.OK, "S-N001", "공지사항 목록을 성공적으로 조회하였습니다."),
	 NOTICE_DETAIL_FOUND(HttpStatus.OK, "S-N002", "공지사항 상세정보를 조회하였습니다."),
	 NOTICE_CREATED(HttpStatus.CREATED, "S-N003", "공지사항이 등록되었습니다."),
	 NOTICE_UPDATED(HttpStatus.OK, "S-N004", "공지사항이 수정되었습니다."),
	 NOTICE_DELETED(HttpStatus.OK, "S-N005", "공지사항이 삭제되었습니다."),
	
	 /* ==============================
	    DIARY (S-Dxxx)
	    ============================== */
	 DIARY_CREATED(HttpStatus.CREATED, "S-D001", "다이어리가 성공적으로 작성되었습니다."),
	 DIARY_UPDATED(HttpStatus.OK, "S-D002", "다이어리가 수정되었습니다."),
	 DIARY_DELETED(HttpStatus.OK, "S-D003", "다이어리가 삭제되었습니다."),
	 DIARY_LIST_FOUND(HttpStatus.OK, "S-D004", "다이어리 목록을 성공적으로 조회하였습니다."),
	 DIARY_DETAIL_FOUND(HttpStatus.OK, "S-D005", "다이어리 상세 정보를 조회하였습니다."),
	 EMOTION_ANALYSIS_COMPLETE(HttpStatus.OK, "S-D006", "감정 분석이 완료되었습니다."),
	
	 /* ==============================
	    REPORT (S-Rxxx)
	    ============================== */
	 REPORT_CREATED(HttpStatus.CREATED, "S-R001", "리포트가 성공적으로 생성되었습니다."),
	 REPORT_UPDATED(HttpStatus.OK, "S-R002", "리포트가 수정되었습니다."),
	 REPORT_DELETED(HttpStatus.OK, "S-R003", "리포트가 삭제되었습니다."),
	 REPORT_LIST_FOUND(HttpStatus.OK, "S-R004", "리포트 목록을 성공적으로 조회하였습니다."),
	 REPORT_DETAIL_FOUND(HttpStatus.OK, "S-R005", "리포트 상세 정보를 조회하였습니다."),
	
	 /* ==============================
	    COMMON (S-Cxxx)
	    ============================== */
	 SUCCESS(HttpStatus.OK, "S-C001", "요청이 성공적으로 처리되었습니다."),
	 CREATED(HttpStatus.CREATED, "S-C002", "리소스가 성공적으로 생성되었습니다."),
	 UPDATED(HttpStatus.OK, "S-C003", "리소스가 성공적으로 수정되었습니다."),
	 DELETED(HttpStatus.OK, "S-C004", "리소스가 성공적으로 삭제되었습니다."),
	 NO_CONTENT(HttpStatus.NO_CONTENT, "S-C005", "콘텐츠가 존재하지 않습니다."),
	 SUCCESS_S3_PRESIGNED_URL(HttpStatus.OK, "S-C006", "URL이 정상적으로 생성되었습니다."),
	
	 /* ==============================
	    EMAIL (S-Exxx)
	    ============================== */
	EMAIL_SEND_SUCCESS(HttpStatus.OK, "S-E001", "인증 이메일 발송에 성공했습니다."),
	EMAIL_VERIFY_SUCCESS(HttpStatus.OK, "S-E001", "이메일 인증에 성공하였습니다."),
	
	
	 /* ==============================
	    Reaction (S-Lxxx)
	    ============================== */
	REACTION_FOUND(HttpStatus.OK, "S-L001", "해당 좋아요가 존재합니다."),
	REACTION_REGISTER_SUCCESS(HttpStatus.OK, "S-L001", "좋아요 등록에 성공했습니다."),
	REACTION_DELETE_SUCCESS(HttpStatus.OK, "S-L001", "좋아요 삭제에 성공했습니다.");
		
	
    private final HttpStatus status;
    private final String code;
    private final String message;
}
