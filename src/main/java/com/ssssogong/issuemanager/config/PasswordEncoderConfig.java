package com.ssssogong.issuemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 비밀번호 인코더 지정 <br>-> 지정 안하면 시큐리티가 실행 안시켜줌..
 */
@Configuration
public class PasswordEncoderConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        /**비밀번호를 그대로 인코딩해주는 인코더 ^^*/
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return (String) rawPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().matches(encodedPassword);
            }
        };
    }
}
