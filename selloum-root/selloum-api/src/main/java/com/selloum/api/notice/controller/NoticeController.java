package com.selloum.api.notice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.api.common.response.ResponseUtil;
import com.selloum.api.notice.dto.NoticeDto;
import com.selloum.api.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
@Tag(name = "Notice", description = "공지사항 관련 API")
public class NoticeController {
	
	private final NoticeService noticeService;
	
	@GetMapping()
	@Operation(summary = "공지사항 목록 조회", description = "현재 죄회 가능한 공지사항 목록을 조회합니다.")
	public ResponseEntity<BaseResponse<List<NoticeDto.response>>> findNoticeList(){
		List<NoticeDto.response> response = noticeService.findNoticeList();
		
		return ResponseUtil.success(ResponseCode.NOTICE_LIST_FOUND, response);
	}
	
	@GetMapping("/{noticeId}")
	@Operation(summary = "공지사항 상세 조회", description = "현재 죄회 가능한 공지사항 목록을 조회합니다.")
	public ResponseEntity<BaseResponse<NoticeDto.response>> findNoticeList( @PathVariable("noticeId") long noticeId){
		NoticeDto.response response = noticeService.findNoticeDetail(noticeId);
		
		return ResponseUtil.success(ResponseCode.NOTICE_LIST_FOUND, response);
	}

}
