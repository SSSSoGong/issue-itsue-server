package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class CommentImageRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentImageRepository commentImageRepository;


    @Test
    @Transactional
    void 해당_코멘트에_달린_이미지_찾기() {
        //given
        Comment comment = Comment.builder()
                .content("코멘트")
                .build();
        commentRepository.save(comment);

        CommentImage commentImage1 = CommentImage.builder()
                .imageUrl("이미지 경로 1")
                .build();

        CommentImage commentImage2 = CommentImage.builder()
                .imageUrl("이미지 경로 2")
                .build();

        commentImageRepository.save(commentImage1);
        commentImageRepository.save(commentImage2);

        //when
        commentImage1.setComment(comment);
        commentImage2.setComment(comment);

        //then
        assertAll(
                () -> assertThat(commentImage1.getComment().getId()).isEqualTo(comment.getId()),
                () -> assertThat(commentImage1.getComment().getId()).isEqualTo(comment.getId())
        );


    }

    @Test
    @Transactional
    void 해당_코멘트에_달린_이미지_삭제() {
        //given
        Comment comment = Comment.builder()
                .content("코멘트")
                .build();
        commentRepository.save(comment);

        CommentImage commentImage1 = CommentImage.builder()
                .imageUrl("이미지 경로 1")
                .build();

        CommentImage commentImage2 = CommentImage.builder()
                .imageUrl("이미지 경로 2")
                .build();

        commentImageRepository.save(commentImage1);
        commentImageRepository.save(commentImage2);

        commentImage1.setComment(comment);
        commentImage2.setComment(comment);

        //when
        commentImageRepository.deleteByCommentId(comment.getId());

        //then
        List<CommentImage> remainingImages = commentImageRepository.findByCommentId(comment.getId());
        assertEquals(0, remainingImages.size());

    }
}