package com.selloum.api.common.response;

import lombok.Getter;

@Getter
public class BaseResponse {
	
	private int status;
    private String code;
    private String message;
    private Object data;

    public static BaseResponse of(ResponseCode responseCode, Object data) {
        return new BaseResponse(responseCode, data);
    }

    public BaseResponse(ResponseCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    } 

}
