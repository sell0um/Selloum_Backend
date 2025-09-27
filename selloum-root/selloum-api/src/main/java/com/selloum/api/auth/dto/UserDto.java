package com.selloum.api.auth.dto;

import lombok.Builder;
import lombok.Data;

public class UserDto {
	
	@Builder
	@Data
	public static class request{
		private long id;
		private String name;
		private String userName;
		private String password;
		private String email;
		private String phone;
		
	}
	
	@Builder
	@Data
	public static class response{
		private long id;
		private String userName;
		private String password;
		private String email;
		private String phone;
		private String profileImage;
	}

}
