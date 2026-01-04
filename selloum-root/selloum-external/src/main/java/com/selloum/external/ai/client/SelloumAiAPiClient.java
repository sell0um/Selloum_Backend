package com.selloum.external.ai.client;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.selloum.external.ai.dto.SelloumAiRequestDto;
import com.selloum.external.ai.dto.SelloumAiResponseDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SelloumAiAPiClient {
	
	private final WebClient selloumAiWebClient;
	
    public SelloumAiResponseDto analyze(SelloumAiRequestDto request) throws Exception {

    	
        return selloumAiWebClient.post()
                .uri("/diaries/analysis") // Uri
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SelloumAiResponseDto.class)
                .timeout(Duration.ofSeconds(90))				// 3초 미응답 시 TimeoutException 발생
                .retry(3)										// 실패 시 3번까지 재 요청 가능
                .onErrorResume(e -> {							// 3차 요청까지 미응답 시 실패 DTO 생성 후 return
                	return Mono.just(
                			SelloumAiResponseDto.builder()
                							    .code("fail")
                							    .build()
            				);
                })
                .block(); // Batch Processor니까 block() 사용 가능
    }

}
