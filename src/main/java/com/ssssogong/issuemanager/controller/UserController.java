package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.FullUserDto;
import com.ssssogong.issuemanager.dto.RegisterRequestDto;
import com.ssssogong.issuemanager.dto.UserDto;
import com.ssssogong.issuemanager.dto.UserResponseDto;
import com.ssssogong.issuemanager.mapper.UserMapper;
import com.ssssogong.issuemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * User 목록 검색 (쿼리스트링 필터 가능)
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findUsers(@RequestParam(value = "name", required = false) String username) {
        // 사용자명이 username과 매치하는 유저 목록을 찾는다.
        List<UserResponseDto> users;
        // 쿼리 스트링이 빈 경우 모든 User 반환
        if (username == null || username.isBlank()) users = new ArrayList<>(userService.findUsers());
            // 그 외엔 필터링한 User 반환
        else users = new ArrayList<>(userService.findUsers(username)); // TODO : query 필드가 여러가지 생기면 어떻게 필터링하지
        return ResponseEntity.ok(users);
    }

    /**
     * login filter 거쳐서 로그인 됨 (여기는 도달할 필요 없음)
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody RegisterRequestDto requestDTO) {
        return ResponseEntity.ok().body("good job");
    }

    /**
     * Body에 입력한 정보로 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDto registerRequest) {
        // DB에 저장한다
        UserResponseDto userDTO;
        try {
            userDTO = userService.save(registerRequest);
        } catch (Exception e) {
            // 이미 존재하는 id인 경우 기각
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        // 회원가입된 정보와 함께 return
        return ResponseEntity.ok(userDTO);
    }

    /**
     * 해당 회원 id 삭제
     */
    @DeleteMapping("/unregister")
    public ResponseEntity<Object> unregister() {
        // 인증 정보에서 accountID를 받아 DB에서 삭제한다.
        UserResponseDto user;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            user = userService.unregister(auth.getName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return ResponseEntity.ok(user);
    }

    /**특정 User가 해당 프로젝트에서 가지는 역할 조회*/
    @GetMapping("/{accountId}/project/{projectId}/role")
    public ResponseEntity<Object> getProjectRole(@PathVariable String accountId, @PathVariable Long projectId) {
        String role = userService.findProjectRoleName(accountId, projectId);
        if (role == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(role);
    }

    /**
     * 해당 id 회원 검색
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Object> getUser(@PathVariable String accountId) {
        UserDto user = userService.findUserByAccountId(accountId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 id입니다.");
        }
        return ResponseEntity.ok(user);
    }

    /**
     * 해당 id 회원 정보 수정
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<Object> updateUser(@RequestBody RegisterRequestDto updateRequest, @PathVariable String accountId) {
        // 사용자명, password에 값 있는 경우 변경
        // TODO: Update 기능
        FullUserDto updated = userService.updateUser(accountId, updateRequest);
        return ResponseEntity.ok(updated);
    }
}
