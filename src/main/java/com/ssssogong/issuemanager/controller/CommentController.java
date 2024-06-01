package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.CommentIdResponseDto;
import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.service.CommentImageService;
import com.ssssogong.issuemanager.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentImageService commentImageService;

    @PostMapping("/issues/{iid}/comments")
    public ResponseEntity<CommentIdResponseDto> createComment(@PathVariable("iid") Long issueId,
                                                              @RequestPart(value = "content", required = false) CommentRequestDto commentRequestDto,
                                                              @ModelAttribute("imageFiles") CommentImageRequestDto commentImageRequestDto) throws IOException {

        CommentIdResponseDto commentIdResponseDto = commentService.createComment(issueId, commentRequestDto);
        commentImageService.saveImages(commentIdResponseDto.getCommentId(), commentImageRequestDto.getImageFiles());

        return new ResponseEntity<>(commentIdResponseDto, HttpStatus.OK);

    }

    @GetMapping("/issues/{iid}/comments")
    public ResponseEntity<List<CommentResponseDto>> findAllComment(@PathVariable("iid") Long issueId) {
        return new ResponseEntity<>(commentService.findAllComment(issueId), HttpStatus.OK);
    }

    @GetMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable("iid") Long issueId,
                                                         @PathVariable("cid") Long commentId) {
        return new ResponseEntity<>(commentService.getComment(commentId), HttpStatus.OK);
    }

    @PutMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<CommentIdResponseDto> updateComment(@PathVariable("iid") Long issueId,
                                                              @PathVariable("cid") Long commentId,
                                                              @RequestPart("content") CommentRequestDto commentRequestDto,
                                                              @ModelAttribute("imageFiles") CommentImageRequestDto commentImageRequestDto) throws IOException {

        commentImageService.deleteImages(commentId);
        commentImageService.saveImages(commentId, commentImageRequestDto.getImageFiles());

        return new ResponseEntity<>(commentService.updateComment(commentId, commentRequestDto.getContent()), HttpStatus.OK);
    }

    @DeleteMapping("/issues/{iid}/comments/{cid}")
    public void deleteComment(@PathVariable("iid") Long issueId,
                              @PathVariable("cid") Long commentId) {

        commentImageService.deleteImages(commentId);
        commentService.deleteComment(commentId);
    }

}
