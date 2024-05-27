package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.ExceptionResponse;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.exception.RoleSettingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final NotFoundException e) {
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(final IllegalArgumentException e) {
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageException(final HttpMessageNotReadableException e){
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.badRequest().build();
    }
  
    @ExceptionHandler(RoleSettingException.class)
    public ResponseEntity<ExceptionResponse> handleRoleSettingException(final RoleSettingException e) {
        log.error("서버 에러 발생", e);
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage("서버 에러 발생").build();
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(final NoResourceFoundException e) {
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage("리소스를 찾을 수 없음").build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(final AccessDeniedException e) {
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        log.error("알 수 없는 에러 발생", e);
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage("알 수 없는 에러 발생").build();
        return ResponseEntity.internalServerError().body(response);
    }
}
