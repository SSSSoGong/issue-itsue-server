package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Test
    @WithMockUser(username = "dev1")
    @Transactional
    void 코멘트_생성하기() {
        //given
        Issue issue = Issue.builder()
                .description("테스트 이슈")
                .build();

        User writer = User.builder()
                .accountId("dev1")
                .build();

        issueRepository.save(issue);
        userRepository.save(writer);


        //when
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("테스트 코멘트");


        //then

        assertThatCode(() -> commentService.createComment(issue.getId(), commentRequestDto))
                .doesNotThrowAnyException();

    }

    @Test
    @WithMockUser(username = "dev1")
    @Transactional
    void 이슈에_속한_모든_코멘트_찾기() {

        //given
        Issue issue = Issue.builder()
                .description("테스트 이슈")
                .build();

        User writer = User.builder()
                .accountId("dev1")
                .build();

        issueRepository.save(issue);
        userRepository.save(writer);

        //when
        Comment comment1 = Comment.builder()
                .issue(issue)
                .content("코멘트1")
                .writer(writer)
                .build();

        Comment comment2 = Comment.builder()
                .issue(issue)
                .content("코멘트2")
                .writer(writer)
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //then
        assertThatCode(() -> commentService.findAllComment(issue.getId()))
                .doesNotThrowAnyException();

    }

    @Test
    @WithMockUser(username = "dev1")
    @Transactional
    void 해당_코멘트_찾기() {
        //given
        Issue issue = Issue.builder()
                .description("테스트 이슈")
                .build();

        User writer = User.builder()
                .accountId("dev1")
                .build();

        issueRepository.save(issue);
        userRepository.save(writer);

        //when
        Comment comment = Comment.builder()
                .writer(writer)
                .issue(issue)
                .content("테스트 코멘트")
                .build();

        commentRepository.save(comment);

        //then
        assertThatCode(() -> commentService.getComment(comment.getId()))
                .doesNotThrowAnyException();

    }

    @Test
    @WithMockUser(username = "dev1")
    @Transactional
    void 코멘트_수정하기() {
        //given
        Issue issue = Issue.builder()
                .description("테스트 이슈")
                .build();

        User writer = User.builder()
                .accountId("dev1")
                .build();

        issueRepository.save(issue);
        userRepository.save(writer);

        Comment comment = Comment.builder()
                .writer(writer)
                .issue(issue)
                .content("테스트 코멘트")
                .build();

        commentRepository.save(comment);

        //when
        String updateContent = "update content";

        //then
        assertThatCode(() -> commentService.updateComment(comment.getId(), updateContent))
                .doesNotThrowAnyException();
    }

    @Test
    @WithMockUser(username = "dev1")
    @Transactional
    void 코멘트_삭제하기() {
        //given
        Issue issue = Issue.builder()
                .description("테스트 이슈")
                .build();

        User writer = User.builder()
                .accountId("dev1")
                .build();

        issueRepository.save(issue);
        userRepository.save(writer);

        Comment comment = Comment.builder()
                .writer(writer)
                .issue(issue)
                .content("테스트 코멘트")
                .build();

        commentRepository.save(comment);

        //when, then
        assertThatCode(() -> commentService.deleteComment(comment.getId())).doesNotThrowAnyException();
    }

}