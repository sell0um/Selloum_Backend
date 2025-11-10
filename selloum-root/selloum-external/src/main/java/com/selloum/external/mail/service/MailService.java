package com.selloum.external.mail.service;

import jakarta.mail.MessagingException;

public interface MailService {
	
    /**
     * 이메일 전송
     *
     * @param emailRequest 이메일 요청 정보
     * @return 전송 성공 여부
     * @throws MessagingException 
     */
	void sendEmail(String email);
	boolean verifyEmail(String email, String verifyCode);
	

}
