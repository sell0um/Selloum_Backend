package com.selloum.api.common.code;

import org.springframework.http.HttpStatus;

import com.selloum.core.code.Status;

public class StatusMapper {
	
    public static HttpStatus toHttpStatus(Status status) {
        try {
            return HttpStatus.valueOf(status.getCode());
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
