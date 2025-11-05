package com.selloum.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.selloum.api",
		"com.selloum.domain",
		"com.selloum.core",
		"com.selloum.external",
})
@EnableJpaRepositories(basePackages = "com.selloum.domain.repository")
@EntityScan(basePackages = "com.selloum.domain.entity")
@EnableJpaAuditing
public class SelloumApllication {
	
	public static void main(String[] args) {
		SpringApplication.run(SelloumApllication.class, args);
	}

}
