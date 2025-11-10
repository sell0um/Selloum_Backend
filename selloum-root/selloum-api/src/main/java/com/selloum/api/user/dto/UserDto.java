package com.selloum.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	@Schema(description = "사용자 요청 DTO")
	public static class request{
		
		private long id;
		
		private String name;

		private String userName;
		
		private String password;
		
		@Email
		private String email;
		
		private String phone;
		
		private String role;
		
	}
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	@Schema(description = "사용자 응답 DTO")
	public static class response{
		private long id;
		private String name;
		private String userName;
		private String password;
		private String email;
		private String phone;
		private String role;
	}

}
