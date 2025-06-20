package com.nonalcohol.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Vue와 통신 시 CSRF 끄기
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/login", "/api/members").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin().disable(); // 프론트엔드에서 처리하므로 formLogin 끔

        return http.build();
    }
}

