package com.selloum.api.common.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.selloum.api.common.code.StatusMapper;
import com.selloum.api.common.response.BaseResponse;
import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;
import com.selloum.core.code.Status;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse<Object>> handleBusinessException(CustomException e) {
    	BaseResponse<Object> response = BaseResponse.of(e.getErrorCode());
        return ResponseEntity
                .status(StatusMapper.toHttpStatus(e.getStatus()))
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleUnexpectedException(Exception e) {
    	BaseResponse<Object>  response = BaseResponse.of(ErrorCode.INTERNAL_ERROR);
        return ResponseEntity
                .status(StatusMapper.toHttpStatus(Status.INTERNAL_SERVER_ERROR))
                .body(response);
    }

}
