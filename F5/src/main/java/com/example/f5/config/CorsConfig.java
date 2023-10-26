package com.example.f5.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:63342", "http://localhost:8080") // 허용할 출처
                    .allowedMethods("GET", "POST") // 허용할 HTTP method
                    .allowedHeaders("Origin","Accept","X-Requested-With","Content-Type","Access-Control-Request-Method","Access-Control-Request-Headers","Authorization")
                    .allowCredentials(true) // 쿠키 인증 요청 허용
                    .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
        }

}
