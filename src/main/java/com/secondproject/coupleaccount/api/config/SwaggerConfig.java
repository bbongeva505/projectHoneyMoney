package com.secondproject.coupleaccount.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurationSupport{
     @Bean
    public OpenAPI dateAccountOpenAPI() {
        Info info = new Info().version("0.0.1").title("데이트통장가계부서비스API").description("데이트통장가계부서비스 Restful API 명세서");
        return new OpenAPI().info(info);
    }    
}
