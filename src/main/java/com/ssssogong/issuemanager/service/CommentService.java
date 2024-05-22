package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentIdResponseDto;
import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Getter
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentImageRepository commentImageRepository;
    private final ImageService imageService;


    private String writerAccountId;

    @Transactional
    public CommentIdResponseDto createComment(Long issueId, CommentRequestDto commentRequestDto, CommentImageRequestDto commentImageRequestDto) throws IOException {

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

        return new CommentIdResponseDto(comment.getId());
    }

    @Transactional
    public List<CommentResponseDto> findAllComment(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IllegalArgumentException::new);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : issue.getComments()) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }

        return commentResponseDtoList;
    }

    @Transactional
    public CommentResponseDto getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentIdResponseDto updateComment(Long commentId, String content, List<MultipartFile> images) throws IOException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);

        comment.update(content); // comment 내용 update

        commentRepository.save(comment); // update 된 comment 저장

        return new CommentIdResponseDto(comment.getId());

    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
        commentRepository.delete(comment);

    }


}


