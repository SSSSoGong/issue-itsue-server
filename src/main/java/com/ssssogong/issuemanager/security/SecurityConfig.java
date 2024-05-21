package com.ssssogong.issuemanager.security;

import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import com.ssssogong.issuemanager.service.CustomUserDetailsService;
import com.ssssogong.issuemanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**Spring Security 설정 클래스*/
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration configuration;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final CustomUserDetailsService userDetailsService;

//    @Bean
//    AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        //provider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(provider);
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 암호화 인코더 인터페이스 지정 -> 지정 안하면 시큐리티에서 오류를 뿜네요..
        return new BCryptPasswordEncoder();  // BCrypt 해시 함수를 사용하는 인코더
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**필터 체인에 대한 설정*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(), jwtUtil);
        loginFilter.setFilterProcessesUrl("/users/login");
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userRepository, userProjectRepository);

        // 접근 권한 설정
        http.authorizeHttpRequests((authorizedHttpRequests) -> authorizedHttpRequests
                .anyRequest().permitAll()
        );
        // 필터 등록
        http
//                .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter 자리에 LoginFilter 등록
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, LoginFilter.class) // LoginFilter 앞에 JwtAuthenticationFilter 등록
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세선 정책 설정 (rest api에 필요하다고 함..)
        ;
        // 기타 설정
        http.formLogin((auth)->auth.disable()) // form 로그인 방식 해제
                .httpBasic((auth) -> auth.disable()) // http basic 해제
                .csrf((auth)->auth.disable()) // csrf 해제
                .headers((headers) -> headers // 프레임 관련 (h2)
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                        ))
                )
        ;

        return http.build();
    }
}
