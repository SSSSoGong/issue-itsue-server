package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    // TODO : 테스트 사진 파일 삭제

    @InjectMocks
    private CommentService commentService;
    // @Mock된 객체 중 필요한 객체 주입해줌

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //Mock 객체 초기화 & field 할당
    }

    @Test
    void 코멘트_생성() throws Exception {
        Long issueId = 1L;
        String writerAccountId = "Jin";
        String content = "냥냥";
        MockMultipartFile mockFile = new MockMultipartFile("image", "cat.jpg", "jpg", new FileInputStream("cat.jpg"));
        //multipartfile의 구현체, mock할 때 사용
        //(post시 해당 파일을 value로 가지는 key 이름, 원래 파일 이름, 파일 형식, 실제 파일 경로)


        Issue issue = mock(Issue.class);
        User writer = mock(User.class);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        when(userRepository.findByAccountId(writerAccountId)).thenReturn(Optional.of(writer));
        // Comment Service 에서 사용할 기본 issue, user 설정


        Constructor<CommentRequestDto> constructor = CommentRequestDto.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CommentRequestDto commentRequestDto = constructor.newInstance();

        Field contentField = CommentRequestDto.class.getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(commentRequestDto, content);

        Field imageFilesField = CommentRequestDto.class.getDeclaredField("imageFiles");
        imageFilesField.setAccessible(true);
        imageFilesField.set(commentRequestDto, List.of(mockFile));

        Field writerAccountIdField = CommentRequestDto.class.getDeclaredField("writerAccountId");
        writerAccountIdField.setAccessible(true);
        writerAccountIdField.set(commentRequestDto, writerAccountId);
        /*
        처음에 CommentRequestDto에서 테스트 때문에 setter 열어뒀다가, 안될거 같아서 없애고 Reflection 이용해서 주입
        사실 이것 저것 찾아보다가 @NoArgsConstructor도 access level private으로 하는게 좋다고 하는걸 봤는데,
        그렇게 하면 값을 어떻게 넣는지 도저히 모르겠어서 일단 풀어놓음
         */


        Long commentId = commentService.createComment(issueId, commentRequestDto);

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class); // Comment type의 객체 가져오기
        verify(commentRepository).save(commentCaptor.capture()); // 그 comment 객체가 레포에 저장 됐으면 pass
        Comment savedComment = commentCaptor.getValue(); // 레포에 저장된 해당 객체 가져오기

        assertThat(savedComment.getContent()).isEqualTo(content);
        assertThat(savedComment.getWriter()).isEqualTo(writer);
        assertThat(savedComment.getIssue()).isEqualTo(issue);
        assertThat(savedComment.getImageUrls()).hasSize(1);
        assertThat(commentId).isEqualTo(savedComment.getId());
    }

    @Test
    void getComment() {
        Long commentId = 1L;
        User writer = mock(User.class);
        when(writer.getAccountId()).thenReturn("Jin");

        Comment comment = mock(Comment.class);
        when(comment.getWriter()).thenReturn(writer);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        CommentResponseDto responseDto = commentService.getComment(commentId);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(comment.getId());
        assertThat(responseDto.getWriterId()).isEqualTo(writer.getAccountId());
    }

    @Test
    void updateComment() throws Exception {
        Long commentId = 1L;


        String content = "농...";
        MockMultipartFile mockFile = new MockMultipartFile("image", "bear.jpg", "jpg", new FileInputStream("bear.jpg"));

        User writer = mock(User.class);
        when(writer.getAccountId()).thenReturn("Jin");

        Comment comment = mock(Comment.class);
        when(comment.getWriter()).thenReturn(writer);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Constructor<CommentRequestDto> constructor = CommentRequestDto.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CommentRequestDto commentRequestDto = constructor.newInstance();

        Field contentField = CommentRequestDto.class.getDeclaredField("content");
        contentField.setAccessible(true);
        contentField.set(commentRequestDto, content);

        Field imageFilesField = CommentRequestDto.class.getDeclaredField("imageFiles");
        imageFilesField.setAccessible(true);
        imageFilesField.set(commentRequestDto, Collections.singletonList(mockFile));


        Long updatedCommentId = commentService.updateComment(commentId, commentRequestDto);

        verify(comment).update(eq(content), anyList());
        assertThat(updatedCommentId).isEqualTo(comment.getId());
    }

    @Test
    void deleteComment() {
        Long commentId = 1L;
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(commentId);

        verify(commentRepository).delete(comment);
    }

    @Test
    void createComment_shouldThrowException_whenIssueNotFound() {
        Long issueId = 1L;
        when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(issueId, new CommentRequestDto());
        });
    }

    @Test
    void createComment_shouldThrowException_whenUserNotFound() throws Exception {
        Long issueId = 1L;
        String writerAccountId = "Jin";
        Issue issue = mock(Issue.class);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(issue));
        when(userRepository.findByAccountId(writerAccountId)).thenReturn(Optional.empty());

        Constructor<CommentRequestDto> constructor = CommentRequestDto.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CommentRequestDto commentRequestDto = constructor.newInstance();

        Field writerAccountIdField = CommentRequestDto.class.getDeclaredField("writerAccountId");
        writerAccountIdField.setAccessible(true);
        writerAccountIdField.set(commentRequestDto, writerAccountId);

        assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(issueId, commentRequestDto);
        });
    }
}
