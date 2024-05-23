package com.ssssogong.issuemanager.security;

import com.ssssogong.issuemanager.domain.account.CustomUserDetails;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import com.ssssogong.issuemanager.service.UserService;
import com.ssssogong.issuemanager.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**jwt 인증 진행 (로그인 되어있는지)*/
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserService userService;

    /**인증 필터 구현*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더 확인
        String authorization = request.getHeader("Authorization");

        // 토큰이 빈 경우 다음으로 넘어간다
        if(authorization == null || !authorization.startsWith("Bearer ")){
            // TODO: Anonymous Authentication 넣어줄까?
            System.out.println("JwtAuthenticationFilter: token null; skipping authorization");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        // username 획득
        String userId = "";
        try {
            userId = jwtUtil.extractAccountData(token).getAccountId();
        }
        catch(Exception e){
            // 토큰 만료, 파싱 에러 등의 경우 인증 없이 넘어감
            System.out.println("JWTAuthFilter : error parsing token " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // User 생성
        Optional<User> user = userRepository.findByAccountId(userId);
        if(user.isEmpty()){
            // User를 찾을 수 없는 경우 인증 없이 넘어감
            System.out.println("JWTAuthFilter : cannot find user " + userId);
            filterChain.doFilter(request, response);
            return;
        }

        // UserDetails에 회원 정보 담음
        CustomUserDetails userDetails = new CustomUserDetails(user.get(), userProjectRepository, userService);

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);        
        // 내용 출력 (디버그용)
        System.out.println("JWTAuthFilter : Hello, " + SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.print("JWTAuthFilter : Your role is ");
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().forEachRemaining(
                (GrantedAuthority auth) -> {
                    System.out.print(auth.getAuthority() + " ");
                }
        );
        System.out.println();

        filterChain.doFilter(request, response);
    }
}
