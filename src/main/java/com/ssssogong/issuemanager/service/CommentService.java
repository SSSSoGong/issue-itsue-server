package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentIdResponseDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.mapper.CommentMapper;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Getter
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;


    private String writerAccountId;

    @Transactional
    public CommentIdResponseDto createComment(Long issueId, CommentRequestDto commentRequestDto) throws IOException {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found"));

        //writerAccountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User writer = userRepository.findByAccountId("Jin")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = CommentMapper.toCommentRequestDto(issue, writer, commentRequestDto);

        commentRepository.save(comment);

        return CommentMapper.toCommentIdResponseDto(comment.getId());
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllComment(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found"));

        return CommentMapper.toCommentResponseDto(issue.getComments());
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Comment not found"));

        return CommentMapper.toCommentResponseDto(comment);
    }

    @Transactional
    public CommentIdResponseDto updateComment(Long commentId, String content) throws IOException {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("Comment not found"));

        comment.update(content); // comment 내용 update

        commentRepository.save(comment); // update 된 comment 저장

        return CommentMapper.toCommentIdResponseDto(commentId);

    }

    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("Comment not found"));

        commentRepository.delete(comment);
    }


}


