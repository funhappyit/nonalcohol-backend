package com.nonalcohol.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 🔧 WebConfig.java
// CORS 설정을 위한 스프링 설정 클래스

@Configuration // 이 클래스가 설정 클래스임을 나타냄 (Spring이 자동으로 읽음)
public class WebConfig implements WebMvcConfigurer {

    // WebMvcConfigurer 인터페이스를 구현하면 필요한 웹 관련 설정을 커스터마이징할 수 있음
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS(Cross-Origin Resource Sharing) 설정
        registry.addMapping("/**") // 모든 URL 패턴에 대해 CORS 적용
                .allowedOrigins("http://localhost:5173") // 허용할 출처(Origin), Vite 개발 서버 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 요청 헤더 허용
                .allowCredentials(true); // 쿠키 포함한 요청 허용
    }
}

