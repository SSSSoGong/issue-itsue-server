package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Getter
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentImageRepository commentImageRepository;

    private String uploadDir;
    private String writerAccountId;

    @Transactional
    public Long createComment(Long issueId, CommentRequestDto commentRequestDto){
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IllegalArgumentException::new);

        writerAccountId = SecurityContextHolder.getContext().getAuthentication().getName();

        User writer = userRepository.findByAccountId(writerAccountId)
                .orElseThrow(IllegalArgumentException::new);

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .issue(issue)
                .writer(writer)
                .build();

        commentRepository.save(comment);


        return new CommentResponseDto(comment).getId();
    }

    @Transactional
    public CommentResponseDto getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public Long updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        comment.update(content);

        return new CommentResponseDto(comment).getId();

    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        commentRepository.delete(comment);
    }


}


