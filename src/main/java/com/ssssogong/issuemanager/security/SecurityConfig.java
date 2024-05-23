package com.ssssogong.issuemanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizedHttpRequests) -> authorizedHttpRequests
                .anyRequest().permitAll()
        );

        http.formLogin((auth) -> auth.disable()) // form 로그인 방식 해제
                .httpBasic((auth) -> auth.disable()) // http basic 해제
                .csrf((auth) -> auth.disable()) // csrf 해제
                .headers((headers) -> headers // 프레임 관련 (h2)
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                        ))
                )
        ;


        return http.build();
    }
}
