package com.selloum.api.diary.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.selloum.api.diary.dto.DiaryDto;

public interface DiaryService {
	
	
	public DiaryDto.response diaryUpload(MultipartFile video,String userName, DiaryDto.request request);
	public List<DiaryDto.response> diaryList(long userId, Integer year, Integer month);
	public DiaryDto.response diaryDetails(long userId, long diaryId);
	public DiaryDto.response updateDiary(Long userId, Long diaryId, DiaryDto.request request);
	public void deleteDiary(long userId, long diaryId);

	public DiaryDto.response updateDiaryVisibility(DiaryDto.request request);
}
