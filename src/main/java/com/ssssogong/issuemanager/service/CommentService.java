package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentIdResponseDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    private String writerAccountId;

    @Transactional
    public CommentIdResponseDto createComment(Long issueId, CommentRequestDto commentRequestDto) throws IOException {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IllegalArgumentException::new);

        //writerAccountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User writer = userRepository.findByAccountId("Jin")
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

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>(); // Comment 받을 리스트 생성

        for (Comment comment : issue.getComments()) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto); // 리스트에 해당 이슈 코멘트 추가
        }

        return commentResponseDtoList;
    }

    @Transactional
    public CommentResponseDto getComment(Long id) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new CommentResponseDto(comment); // 해당하는 코멘트 반환
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


