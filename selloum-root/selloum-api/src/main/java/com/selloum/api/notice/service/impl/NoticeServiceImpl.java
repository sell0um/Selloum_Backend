package com.selloum.api.notice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.selloum.api.notice.dto.NoticeDto;
import com.selloum.api.notice.dto.NoticeDto.response;
import com.selloum.api.notice.service.NoticeService;
import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;
import com.selloum.domain.entity.Notice;
import com.selloum.domain.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRespository;

	@Override
	public List<NoticeDto.response> findNoticeList() {
		List<Notice> list = noticeRespository.findAll();
		
		if(list.isEmpty()) {
			throw new CustomException(ErrorCode.NOTICE_NOT_FOUND);
		}
		
		return list.stream()
				   .map(this:: toResponse)
				   .collect(Collectors.toList());
	}

	@Override
	public response findNoticeDetail(long noticeId) {
		Notice notice = noticeRespository.findById(noticeId).orElseThrow(
					() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND)
				);
		
		return toResponse(notice);
	}
	
	
	
	
	private NoticeDto.response toResponse(Notice notice){
		return NoticeDto.response.builder()
								 .noticeId(notice.getNoticeId())
								 .name(notice.getName())
								 .content(notice.getContent())
								 .imagePath(notice.getImagePath())
								 .build();
	}
}
