package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/issues/{iid}/comments")
    public ResponseEntity<Long> createComment(@PathVariable("iid") Long issueId,
                                              @RequestBody CommentRequestDto commentRequestDto,
                                              @ModelAttribute CommentImageRequestDto commentImageRequestDto) throws IOException {

        Long commentId = commentService.createComment(issueId, commentRequestDto, commentImageRequestDto);
        if (commentId != null)
            return new ResponseEntity<>(commentId, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/issues/{iid}/comments")
    public ResponseEntity<List<CommentResponseDto>> findAllComment(@PathVariable("iid") Long issueId,
                                                                   @RequestBody CommentRequestDto commentRequestDto,
                                                                   @ModelAttribute CommentImageRequestDto commentImageRequestDto) {
        return new ResponseEntity<>(List.of(commentService.getComment(issueId)), HttpStatus.OK);
    }

    @GetMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable("iid") Long issueId,
                                                         @PathVariable("cid") Long commentId) {
        return new ResponseEntity<>(commentService.getComment(commentId), HttpStatus.OK);
    }

    @PutMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<Long> updateComment(@PathVariable("iid") Long issueId,
                                              @PathVariable("cid") Long commentId,
                                              @RequestPart String content,
                                              @RequestPart List<MultipartFile> images) throws IOException {

        commentService.updateComment(commentId, content, images);

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @DeleteMapping("/issues/{iid}/comments/{cid}")
    public void deleteComment(@PathVariable("iid") Long issueId,
                              @PathVariable("cid") Long commentId) {
        commentService.deleteComment(commentId);
    }

}
