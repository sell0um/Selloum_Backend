package com.selloum.api.diary.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.selloum.api.auth.domain.CustomUserDetails;
import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.api.common.response.ResponseUtil;
import com.selloum.api.diary.dto.DiaryDto;
import com.selloum.api.diary.service.DiaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "diary", description = "다이어리 관련 API")
@RequestMapping("/diary")
public class DiaryController {
	
	private final DiaryService diaryService;
	
	@Operation(summary = "다이어리 등록", description = "다이어리 등록 API")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@PostMapping("/diaries")
	public ResponseEntity<BaseResponse<DiaryDto.response>> diaryUpload(
								@RequestPart("video") MultipartFile video,
								@AuthenticationPrincipal CustomUserDetails userDetails,
								@RequestPart("request") @Valid DiaryDto.request req)  {
		
		DiaryDto.response response = diaryService.diaryUpload(video, userDetails.getUser().getUsername(), req);
		
		return ResponseUtil.success(ResponseCode.DIARY_CREATED, response);
	}
	
	
	
	@Operation(summary = "다이어리 목록", description = "다이어리 목록 조회 API")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@GetMapping("/diaries")
	public ResponseEntity<BaseResponse<List<DiaryDto.response>>> diaryList(@AuthenticationPrincipal CustomUserDetails userDetails,
																		   DiaryDto.dailyFIlter diaryFIlter)  {
		
		System.out.println(diaryFIlter.getYear() + " / " + diaryFIlter.getMonth());
				
		List<DiaryDto.response> response = diaryService.diaryList(userDetails.getUser().getId(), diaryFIlter.getYear(), diaryFIlter.getMonth());
		
		return ResponseUtil.success(ResponseCode.DIARY_LIST_FOUND, response);
	}
	
	
	@Operation(summary = "다이어리 상세", description = "다이어리 상세 조회 API")
	@ApiResponses({		
			@ApiResponse(responseCode = "200", description = "성공"),
			@ApiResponse(responseCode = "400" , description = "실패")
			
	})
	@GetMapping("/diaries/{diaryId}")
	public ResponseEntity<BaseResponse<DiaryDto.response>> diaryDetails(@AuthenticationPrincipal CustomUserDetails userDetails,
																		@PathVariable("diaryId") Long diaryId)  {
		
		DiaryDto.response response = diaryService.diaryDetails(userDetails.getUser().getId(), diaryId);
		
		return ResponseUtil.success(ResponseCode.DIARY_CREATED, response);
	}
	
	
	
	
	
	@Operation(summary = "다이어리 삭제", description = "다이어리 삭제 API")
	@ApiResponses({		
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400" , description = "실패")
		
	})
	
	@DeleteMapping("/diaries/{diaryId}")
	public ResponseEntity<BaseResponse<String>> deleteDiary(@AuthenticationPrincipal CustomUserDetails userDetails,
																		@PathVariable("diaryId") Long diaryId)  {
		
		diaryService.deleteDiary(userDetails.getUser().getId(), diaryId);
		
		return ResponseUtil.success(ResponseCode.DIARY_DELETED, "SUCCESS");
	}
	
	
	/*
	 * 다이어리 수정 파트는 마지막에 합시다
	 */
	
	@Operation(summary = "다이어리 수정", description = "다이어리 수정 조회 API")
	@ApiResponses({		
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400" , description = "실패")
		
	})
	@PutMapping("/diaries/{diaryId}")
	public ResponseEntity<BaseResponse<DiaryDto.response>> updateDiary(@AuthenticationPrincipal CustomUserDetails userDetails,
			@PathVariable("diaryId") Long diaryId,
			@Valid @RequestBody DiaryDto.request req)  {
		
		DiaryDto.response response = diaryService.updateDiary(userDetails.getUser().getId(), diaryId, req);
		
		return ResponseUtil.success(ResponseCode.DIARY_CREATED, response);
	}

}
