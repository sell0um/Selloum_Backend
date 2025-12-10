package com.selloum.api.diary.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DiaryDto {
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class request{
		
		private long id;
		private String content;
		private boolean isPublic;
		private boolean isComplaint;
	}
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class response{
		
		private long id;
		private String content;
		private String emotion;
		private String diaryFilePath;
		private boolean isPublic;
		private boolean isComplaint;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
		
		
	}

	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class dailyFIlter {
	     private Integer year;
	     private Integer month;
	}
}
