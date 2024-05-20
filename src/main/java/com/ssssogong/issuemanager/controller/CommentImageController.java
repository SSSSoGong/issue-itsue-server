package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.service.CommentImageService;
import com.ssssogong.issuemanager.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentImageController {

    private final CommentImageService commentImageService;
    @PostMapping("/comments/{cid}/images")
    public ResponseEntity<Long> createComment(@PathVariable("cid") Long commentId, @ModelAttribute CommentImageRequestDto commentImageRequestDto) throws IOException {

        commentImageService.createImages(commentId, commentImageRequestDto.getImageFiles());
        if (commentId != null)
            return new ResponseEntity<>(commentId, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
