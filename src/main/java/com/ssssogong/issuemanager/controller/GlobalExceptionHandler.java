package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.ExceptionResponse;
import com.ssssogong.issuemanager.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        log.error("알 수 없는 에러 발생", e);
        final ExceptionResponse response = ExceptionResponse.builder().errorMessage("알 수 없는 에러 발생").build();
        return ResponseEntity.internalServerError().body(response);
    }
}
