package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/issues/{iid}/comments")
    public ResponseEntity<Long> createComment(@PathVariable("iid") Long issueId, @ModelAttribute CommentRequestDto commentRequestDto) throws IOException {

        Long commentId = commentService.createComment(issueId, commentRequestDto);
        if (commentId != null)
            return new ResponseEntity<>(commentId, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/issues/{iid}/comments/{cid}")
//    public ResponseEntity<CommentResponseDto> getComment() {
//
//    }
//
//    @PutMapping("/issues/{iid}/comments/{cid}")
//    public ResponseEntity<CommentResponseDto> updateComment() {
//
//    }
//
//    @DeleteMapping("/issues/{iid}/comments/{cid}")
//    public ResponseEntity<CommentResponseDto> deleteComment() {
//
//    }

}
