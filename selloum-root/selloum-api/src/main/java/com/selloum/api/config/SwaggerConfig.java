package com.selloum.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

@OpenAPIDefinition(
//		   info = @Info(
//		      title = "Selloum Diary API 명세서",
//		      description = "Selloum Diary Backend API 명세서",
//		      version = "v1"
//		   )
		)
@Configuration
public class SwaggerConfig {
	
	@Value("$jwt.prefix")
	String JWT_TOKEN_PREFIX;
	
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }
    
    
    private Info info() {
    	return new Info().title("Selloum API")
    					 .description("Selloum Diary 백엔드 API 명세서")
    					 .version("v1.0")
    					 .summary("Selloum API 명세서");
    }

}
