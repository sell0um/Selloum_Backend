package com.selloum.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmailDto {
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class sendRequest{

        @NotNull @NotEmpty(message = "이메일을 입력해야 합니다.")
        @NotBlank(message = "이메일을 입력하세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;
	}
	
	
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class verifyRequest{
		
        @NotNull @NotEmpty(message = "인증 코드를 입력해야합니다.")
        @NotBlank(message = "인증 코드를 입력하세요")
        private String verifyCode;

        @NotNull @NotEmpty(message = "이메일을 입력해야 합니다.")
        @NotBlank(message = "이메일을 입력하세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        private String email;
	}

	
	
	
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class response{
		
        private String verifyCode;
        private Boolean result;
		
	}

}
