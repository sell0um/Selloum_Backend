package com.selloum.api.auth.jwt;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.selloum.core.Exception.CustomException;
import com.selloum.core.code.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTokenUtils {
	
	private final RedisTemplate<String, String> redisTemplate;
	private final JwtTokenProvider jwtTokenProvider;
	
	@Value("${jwt.header.access}")
	private String accessTokenHeader;
	@Value("${jwt.header.refresh}")
	private String refreshTokenHeader;
	
	
	@Value("${mail.prefix.reset-verify}")
	private String redisKeyResetVerifyPrefix;
	
	private static final String REFRESH_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";
    
    /****************************************
      Access , RefreshToken 관련 Util Method
     ****************************************/
	
	/** Refresh Token 저장 */
    public void saveRefreshToken(String username, String refreshToken, long ttlMillis) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + username, refreshToken, Duration.ofMillis(ttlMillis));
    }

    /** Refresh Token 조회 */
    public String getRefreshToken(Long userId) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + userId);
    }

    /** Refresh Token 삭제 */
    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(REFRESH_PREFIX + userId);
    }

    /** Access Token 블랙리스트 등록 */
    public void addToBlacklist(String accessToken, long expirationMillis) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + accessToken, "logout", Duration.ofMillis(expirationMillis));
    }

    /** 블랙리스트 토큰 확인 */
    public boolean isBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken));
    }
    
    
    public boolean isVerifiedResetCodeEmail(String email) {
    	
    	String result = redisTemplate.opsForValue().get(redisKeyResetVerifyPrefix + email);
    	
    	if(result == null || !result.equals("true")) { // 없거나 인증되지 않았다면
    		throw new CustomException(ErrorCode.EMAIL_VERIFY_FAILED);
    	}
    	return true;
    }
    
    
    

}
