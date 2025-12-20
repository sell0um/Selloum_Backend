package com.selloum.api.notice.dto;

import com.selloum.api.user.dto.EmailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NoticeDto {

	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class response{
		private long noticeId;
		private String name;
		private String content;
		private String imagePath;
	}

}
