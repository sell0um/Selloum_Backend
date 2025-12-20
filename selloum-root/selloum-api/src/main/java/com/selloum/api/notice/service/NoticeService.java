package com.selloum.api.notice.service;

import java.util.List;

import com.selloum.api.notice.dto.NoticeDto;

public interface NoticeService {
	
	List<NoticeDto.response> findNoticeList();
	NoticeDto.response findNoticeDetail(long noticeId);
	
	

}
