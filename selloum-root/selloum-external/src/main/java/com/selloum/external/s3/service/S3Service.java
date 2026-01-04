package com.selloum.external.s3.service;

public interface S3Service {
	
	String generateUploadUrl(String key, String contentType);

	String generateDownloadUrl(String key);

	
}
