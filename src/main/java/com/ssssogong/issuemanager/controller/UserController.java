package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> findUsers(){
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(){
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(){
        return null;
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<Object> unregister(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(){
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(){
        return null;
    }
}
