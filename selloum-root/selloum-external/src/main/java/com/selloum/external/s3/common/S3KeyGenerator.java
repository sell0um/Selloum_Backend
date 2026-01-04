package com.selloum.external.s3.common;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class S3KeyGenerator {
	
	public String generate(
            String domain,
            String resourceId,
            String originalFilename
    ) {
        String extension = extractExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return String.format(
            "%s/%s/%s.%s",
            domain,
            resourceId,
            uuid,
            extension
        );
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

}
