package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@Sql(value = "/truncate.sql")
class CommentImageRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentImageRepository commentImageRepository;


    @Test
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

        commentRepository.save(comment);


        //then
        List<CommentImage> commentImageList = commentImageRepository.findByCommentId(comment.getId());
        assertThat(commentImageList.size()).isEqualTo(2);
    }

    @Test
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
        assertEquals(0, commentImageRepository.count());
    }
}