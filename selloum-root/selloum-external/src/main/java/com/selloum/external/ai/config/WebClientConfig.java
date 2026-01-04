package com.selloum.external.ai.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {
	
	@Bean
	public WebClient selloumAiWebClient(WebClient.Builder builder) {
		
		HttpClient httpClient = HttpClient.create()
										.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
										.responseTimeout(Duration.ofMillis(5000))
										.doOnConnected(
												conn -> conn.addHandlerLast(new ReadTimeoutHandler(10))
															.addHandlerLast(new WriteTimeoutHandler(10))
												);
		
		
		WebClient client = builder.baseUrl("http://localhost:9090") //AI Server URL
								  .defaultHeader(HttpHeaders.CONTENT_TYPE, "multipart/form-data")
								  .filter(
					                        ExchangeFilterFunction.ofRequestProcessor(
					                                clientRequest -> {
					                                    log.info(">>>>>>>>> FastAPI - REQUEST <<<<<<<<<<");
					                                    log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
					                                    clientRequest.headers().forEach(
					                                            (name, values) -> values.forEach(value -> log.info("{} : {}", name, value))
					                                    );
					                                    return Mono.just(clientRequest);
					                                }
					                        )
					                )
					                //Response Header 로깅 필터
					                .filter(
					                        ExchangeFilterFunction.ofResponseProcessor(
					                                clientResponse -> {
					                                    log.info(">>>>>>>>>> FastAPI - RESPONSE <<<<<<<<<<");
					                                    clientResponse.headers().asHttpHeaders().forEach(
					                                            (name, values) -> values.forEach(value -> log.info("{} {}", name, value))
					                                    );
					                                    return Mono.just(clientResponse);
					                                }
					                        )
					                )
								  .clientConnector(new ReactorClientHttpConnector(httpClient))				
  								  .build();
		
		return client;
	}

}
