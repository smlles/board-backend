package com.example.jh.project.first_board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000",
                    		"http://www.springbootjyh.shop",
                    		"https://www.springbootjyh.shop",
                    		"http://board-back-end-env.eba-uh9hqpkz.ap-northeast-2.elasticbeanstalk.com",
                    		"https://board-back-end-env.eba-uh9hqpkz.ap-northeast-2.elasticbeanstalk.com")  // React 개발 서버
                    .allowedMethods("*","GET","POST","PUT","DELETE","OPTIONS")
                    .allowedHeaders("*")
                    
                    .allowCredentials(true);
            }
        };
    }
}