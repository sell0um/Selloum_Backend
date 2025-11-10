package com.selloum.api.auth.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.selloum.api.auth.service.TokenBlackListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(TokenBlackListServiceImpl.class);
	private final RedisTemplate<String, Object> redisTemplate;
	
	
	@Value("${spring.data.redis.blacklist-key}")
	private String REDIS_BLACKLIST_KEY;
	

    /**
     * BlackList 내에 토큰을 추가
     *
     * @param value
     */
	@Override
	public void addTokenToList(String value) {
		  redisTemplate.opsForList().rightPush(REDIS_BLACKLIST_KEY, value);
	}
	
	/**
	 * Redis의 key 기반으로 값들을 조회하는 
	 * 
	 * @param value
	 * @return boolean
	 */

	@Override
	public boolean isContainToken(String value) {
		
		List<Object> items = getTokenBlackList();
		
		return items.stream().anyMatch(item -> item.equals(value));
	}

	/**
	 * redis에 저장된 블랙리스트 전체를 조회
	 * 
	 * @return List<Object>
	 */
	@Override
	public List<Object> getTokenBlackList() {
		return redisTemplate.opsForList().range(REDIS_BLACKLIST_KEY, 0, -1);
	}

	
	/**
	 * 파라미터로 전달받은 값을 블랙리스트에서 삭제
	 * 
	 * @param value
	 */

	@Override
	public void removeToken(String value) {
		redisTemplate.opsForList().remove(REDIS_BLACKLIST_KEY, 0, value);
		
	}


}
