package com.selloum.external.mail.service.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.selloum.core.Exception.CustomException;
import com.selloum.core.util.VerificationCodeGenerator;
import com.selloum.external.mail.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

	private final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
	private final JavaMailSender mailSender;
	private final RedisTemplate<String, Object> redisTemplate;
	private final VerificationCodeGenerator verificationCodeGenerator;
	
	@Value("${mail.prefix}")
	private String redisKeyPrefix;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private int expirationMillis;
	private String mailSubject = "[Selloum]에서 보낸 이메일 인증 번호  입니다."; 
	

	@Override
	public void sendEmail(String email) {
		
		LOGGER.info("[ MailServiceImpl ] - sendEmail() ");

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true);
			
			
			String verificationCode = verificationCodeGenerator.generate();
			String redisKey = redisKeyPrefix + email;
	        redisTemplate.opsForValue().set(
	            redisKey, 
	            verificationCode, 
	            expirationMillis, 
	            TimeUnit.MINUTES
	        );
			String content = createEmailForm(verificationCode);
			
			helper.setTo(email);
			helper.setSubject(mailSubject);
			helper.setText(content, true);
			
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new CustomException();
		}
		
		
	}
	
	
	@Override
	public boolean verifyEmail(String email, String verifyCode) {
		
		LOGGER.info(" [ MailServiceImpl ] - verifyEmail() ");

		// 1. rediskey 생성후 인증 코드 가져오기
		String redisKey = redisKeyPrefix + email;
		String savedCode = (String)redisTemplate.opsForValue().get(redisKey);
		
		// 2. 코드가 없으면 (만료되었거나 존재하지 않음)
		if(savedCode == null) {
			throw new CustomException();
		}
		
		// 3. 입력 코드와 실제 인증 코드 비교
		boolean isValid = savedCode.equals(verifyCode);
		
		// 4. 인증 성공 - Redis에서 삭제 (재사용 방지)
		if(isValid) redisTemplate.delete(redisKey);
		
		
		return isValid;
	}
	
	
	
	/*********************
	  Mail 관련 Util 메서드
	 *********************/
	
	private String createEmailForm(String verificationCode) {
		
		String emailFormString = "<html>"
				+ "<body>"
				+ "    <h1>Selloum 이메일 인증 번호</h1>"
				+ "    <p>Selloum 인증 코드: " + verificationCode  +"</p>"
				+ "    <p>해당 코드를 홈페이지에 입력하세요."
				+ "    <footer style=\"color: grey; font-size: small;\">"
				+ "        <p> **본 메일은 자동응답 메일로, 본 메일에 회신하지 마시길 바랍니다.</p>"
				+ "    </footer>"
				+ "</body>"
				+ "</html>";
		
		
		return emailFormString;
		
	}




	
	

}
