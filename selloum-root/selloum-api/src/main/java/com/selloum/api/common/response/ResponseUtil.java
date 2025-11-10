package com.selloum.api.common.response;

import org.springframework.http.ResponseEntity;

import com.selloum.api.common.code.ResponseCode;

public class ResponseUtil {
	
	public static <T> ResponseEntity<BaseResponse<T>> success(ResponseCode code, T data){
		
		return ResponseEntity.status(code.getStatus())
				.body(BaseResponse.of(code, data));
	}
	
	
//	public static <T> ResponseEntity<BaseResponse<T>> error(ErrorCode code, T data){
//		
//		return ResponseEntity.status(code.getStatus())
//				.body(BaseResponse.of(code, data));
//	}

}
