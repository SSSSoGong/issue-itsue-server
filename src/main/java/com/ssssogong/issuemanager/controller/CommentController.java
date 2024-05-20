package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/issues/{iid}/comments")
    public ResponseEntity<Long> createComment(@PathVariable("iid") Long issueId, CommentRequestDto commentRequestDto) {

        Long commentId = commentService.createComment(issueId, commentRequestDto);
        if (commentId != null)
            return new ResponseEntity<>(commentId, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable("iid") Long issueId,
                                                         @PathVariable("cid") Long commentId) {
        return new ResponseEntity<>(commentService.getComment(commentId), HttpStatus.OK);
    }

    @PutMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<Long> updateComment(@PathVariable("iid") Long issueId,
                                              @PathVariable("cid") Long commentId,
                                              @RequestBody String content) {

        commentService.updateComment(commentId,content);

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @DeleteMapping("/issues/{iid}/comments/{cid}")
    public void deleteComment(@PathVariable("iid") Long issueId,
                              @PathVariable("cid") Long commentId) {
        commentService.deleteComment(commentId);
    }

}
