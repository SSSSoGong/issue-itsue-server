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
import com.ssssogong.issuemanager.security.SecurityConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private CommentIdResponseDto commentIdResponseDto;

    @InjectMocks
    private CommentService commentService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    @WithMockUser
    public void testCreateComment() throws IOException {
        Long issueId = 1L;
        String writerAccountId = "Jin";

        Issue issue = Issue.builder()
                .id(issueId).build();

        User writer = User.builder()
                .accountId(writerAccountId).build();


        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        when(userRepository.findByAccountId(writerAccountId)).thenReturn(Optional.of(writer));
        // Comment Service 에서 사용할 기본 issue, user 설정

        when(authentication.getName()).thenReturn(writerAccountId);
        // 시큐리티에서 가져올 이름 설정

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("Test comment content");

        commentIdResponseDto = commentService.createComment(1L, commentRequestDto);


        assertEquals(1L, commentIdResponseDto.getCommentId());
    }

    @Test
    public void testFindAllComment() {

        Comment comment1 = Comment.builder().id(1L).content("Test comment 1").build();
        Comment comment2 = Comment.builder().id(2L).content("Test comment 2").build();
        Issue issue = Issue.builder().id(1L)
                .comments(List.of(comment1, comment2))
                .build();

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));

        List<CommentResponseDto> result = commentService.findAllComment(1L);

        assertEquals(2, result.size());
        assertEquals("Test comment 1", result.get(0).getContent());
        assertEquals("Test comment 2", result.get(1).getContent());
    }

    @Test
    public void testGetComment() {

        Comment comment = Comment.builder().id(1L).content("Test comment").build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentResponseDto response = commentService.getComment(1L);

        assertEquals("Test comment", response.getContent());
    }

    @Test
    public void testUpdateComment() throws IOException {

        Comment comment = Comment.builder().id(1L).content("Old comment content").build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentIdResponseDto response = commentService.updateComment(1L, "New comment content", Collections.emptyList());

        assertEquals(1L, response.getCommentId());
        assertEquals("New comment content", comment.getContent());
    }

    @Test
    public void testDeleteComment() {

        Comment comment = Comment.builder().id(1L).build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).delete(comment);
    }
}