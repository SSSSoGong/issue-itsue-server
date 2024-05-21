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
@RestController
public class CommentController {

    private final CommentService commentService;

    // TODO : 어노테이션 잘 들어가는지 확인
    @PostMapping("/issues/{iid}/comments")
    public ResponseEntity<Long> createComment(@PathVariable("iid") Long issueId,
                                              @RequestPart("content") CommentRequestDto commentRequestDto,
                                              @ModelAttribute("imageFiles") CommentImageRequestDto commentImageRequestDto) throws IOException {

        Long commentId = commentService.createComment(issueId, commentRequestDto, commentImageRequestDto);
        if (commentId != null)
            return new ResponseEntity<>(commentId, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/issues/{iid}/comments-all/{cid}")
    public ResponseEntity<List<CommentResponseDto>> findAllComment(@PathVariable("iid") Long issueId,
                                                                   @PathVariable("cid") Long commentId) {
        return new ResponseEntity<>(commentService.findAllComment(issueId), HttpStatus.OK);
    }

    @GetMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable("iid") Long issueId,
                                                         @PathVariable("cid") Long commentId) {
        return new ResponseEntity<>(commentService.getComment(commentId), HttpStatus.OK);
    }

    @PutMapping("/issues/{iid}/comments/{cid}")
    public ResponseEntity<Long> updateComment(@PathVariable("iid") Long issueId,
                                              @PathVariable("cid") Long commentId,
                                              @RequestPart("content") CommentRequestDto commentRequestDto,
                                              @ModelAttribute List<MultipartFile> imageFiles) throws IOException {

        commentService.updateComment(commentId, commentRequestDto.getContent(), imageFiles);

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @DeleteMapping("/issues/{iid}/comments/{cid}")
    public void deleteComment(@PathVariable("iid") Long issueId,
                              @PathVariable("cid") Long commentId) {
        commentService.deleteComment(commentId);
    }

}
