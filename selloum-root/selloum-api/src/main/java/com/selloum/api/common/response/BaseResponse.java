package com.selloum.api.common.response;

import com.selloum.api.common.code.ResponseCode;
import com.selloum.core.code.ErrorCode;

import lombok.Getter;

@Getter
public class BaseResponse<T> {
	
    private String code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> of(ResponseCode responseCode, T data) {
        return new BaseResponse<>(responseCode, data);
    }
    
    public static <T> BaseResponse<T> of(ErrorCode responseCode) {
        return new BaseResponse<>(responseCode,null);
    }

    public BaseResponse(ResponseCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    } 
    
    public BaseResponse(ErrorCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    } 

}
