package com.selloum.external.ai.dto;

import java.io.File;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SelloumAiRequestDto {
	
	private String s3PreSignedUrl;

}
