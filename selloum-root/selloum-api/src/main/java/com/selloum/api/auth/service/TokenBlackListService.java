package com.selloum.api.auth.service;

import java.util.List;

public interface TokenBlackListService {
	
	
	void addTokenToList(String value);  
	boolean isContainToken(String value); 
	List<Object> getTokenBlackList();
	void removeToken(String value);


}
