package com.ssssogong.issuemanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssssogong.issuemanager.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**로그인 요청을 가로채서 검증 & 인증 진행*/
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private long tokenExpiredMs = 60*60*1000L;

    public void setTokenExpiredMs(long tokenExpiredMs){
        if(tokenExpiredMs < 0 ) return;
        this.tokenExpiredMs = tokenExpiredMs;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 로그인 요청 가로채서 검증 진행

        // 요청에서 id, password 추출
        String id, password;
        if(request.getHeader("content-type").startsWith("application/json")) {
            try {
                // json으로 요청한 경우 -> 파싱해서 꺼낸다
                BufferedReader br = request.getReader();
                String jsonString = br.lines().collect(Collectors.joining(System.lineSeparator()));
                Map<String, String> jsonRequest = new ObjectMapper().readValue(jsonString, Map.class);
                id = jsonRequest.get("id");
                password = jsonRequest.get("password");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            // form으로 요청한 경우 -> 파라미터에서 꺼낸다
            id = request.getParameter("id");
            password = request.getParameter("password");
        }

        System.out.println("LoginFilter: Attempting Authentication - id : '" + id + "' password : '" + password + "'"); // test

        // 스프링 시큐리티에서 username과 password를 검증하기 위해서 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password, null);

        // token 검증을 위해 AuthenticationManager에 전달
        return authenticationManager.authenticate(authToken);
    }

    /**로그인 성공 시 호출되는 함수<br>
     * Response Body에 "Bearer ~~"의 형태로 jwt 발급*/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        // 로그인 성공
        System.out.println("Attempt successful.");

        String id = authentication.getName();
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        System.out.println("LoginFilter: Hello, " + id + " with role ["); // test
        for(GrantedAuthority auth: authorities){
            System.out.print(auth.getAuthority() + " ");
        }
        System.out.println();

        String token = jwtUtil.createToken(id, false); // TODO : JWT에 어떤 정보 넣을지

        try {
            // body로 jwt 발급
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter writer = response.getWriter();
            Map<String, String> jwtBody = new HashMap<>();
            jwtBody.put("authorization", "Bearer " + token);
            writer.println(mapper.writeValueAsString(jwtBody));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**로그인 실패 시 호출되는 함수*/
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        // 로그인 실패
        System.out.println("Attempt failed.");
        response.setStatus(401);
    }
}
