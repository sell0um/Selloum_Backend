package com.selloum.api.diary.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
import com.selloum.api.common.code.ResponseCode;
import com.selloum.api.diary.dto.DiaryDto;
import com.selloum.api.diary.dto.DiaryDto.response;
import com.selloum.api.diary.service.DiaryService;
import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;
import com.selloum.domain.entity.Diary;
import com.selloum.domain.entity.User;
import com.selloum.domain.repository.DiaryRepository;
import com.selloum.domain.repository.UserRepository;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
	
	private final DiaryRepository diaryRepository;
	private final UserRepository userRepository;
	
	@Override
	public DiaryDto.response diaryUpload(MultipartFile video, String userName ,DiaryDto.request request) {
		
		// User 정보 찾기
		User user = userRepository.findByUsername(userName)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		
		
		// S3 파일 저장 -> 일단 하드코딩
		String diaryFilePath = "diaryFilePath in S3";
		
		Diary diary = Diary.builder()
						   .diaryFilePath(diaryFilePath)
						   .isPublic(request.isPublic())
						   .user(user)
						   .build();
		
		try {
			diary = diaryRepository.save(diary);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.DIARY_CREATE_FAILED);
		}
		
		return toResponse(diary);
	}

	
	
	@Override
	public List<DiaryDto.response> diaryList(long userId, Integer year, Integer month) {
		
		System.out.println(userId + " / " + year + " / " + month);

		
		try{
			List<Diary> diaries = diaryRepository.findAllByUserAndDate(userId, year, month);
			
			System.out.println(diaries.size());
			
			return diaries.stream()
					.map(this::toResponse)
					.toList();
			
		} catch (Exception e) {
			throw new CustomException(ErrorCode.DIARY_NOT_FOUND);
		}
		
	}

	@Override
	public response diaryDetails( long userId, long diaryId) {
		Diary diary = diaryRepository.findById(diaryId).orElseThrow(
				() -> new CustomException(ErrorCode.DIARY_NOT_FOUND));
		
		try {
			
			if(!diary.isPublic() && diary.getUser().getId() != userId) { // 비공개인데 아이디가 일치하지 않으면
				throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
			}
			
		} catch (Exception e) {
			 new CustomException(ErrorCode.DIARY_NOT_FOUND);
		}
		
		return toResponse(diary);
	}

	@Override
	public void deleteDiary(long userId, long diaryId) {
		
		Diary diary = diaryRepository.findById(diaryId).orElseThrow(
					() -> new CustomException(ErrorCode.DIARY_NOT_FOUND)
				);
		
		if(diary.getUser().getId() != userId){ // 작성자가 아니면 삭제 불가
			throw new CustomException(ErrorCode.DIARY_ACCESS_DENIED);
		}
		
		diaryRepository.delete(diary);
		
	}





	@Override
	public response updateDiaryVisibility(DiaryDto.request request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public response updateDiary(Long userId, Long diaryId, DiaryDto.request request) {
		
		Diary diary = diaryRepository.findById(diaryId).orElseThrow(
				() -> new CustomException(ErrorCode.DIARY_NOT_FOUND)
				);
		
		
		
		return null;
		
	}
	
	
	
	/* Service 단 메서드*/
	private DiaryDto.response toResponse(Diary diary) {
		
		
		return DiaryDto.response.builder()
								.id(diary.getDiaryId())
								.content(diary.getContent())
								.diaryFilePath(diary.getDiaryFilePath())
								.emotion((diary.isAnalyzed()) ? diary.getEmotion().getEmotion() : "")
								.isPublic(diary.isPublic())
								.createdAt(diary.getCreatedAt())
								.modifiedAt(diary.getModifiedAt())
								.build();
	}
	

}
