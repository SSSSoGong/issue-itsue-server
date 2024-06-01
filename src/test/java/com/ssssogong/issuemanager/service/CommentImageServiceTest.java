package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class CommentImageServiceTest {

    @Autowired
    private CommentImageService commentImageService;

    @Autowired
    private CommentImageRepository commentImageRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Comment getComment() {
        Comment comment = Comment.builder()
                .content("기본 코멘트")
                .build();

        commentRepository.save(comment);

        return comment;
    }

    @Test
    @Transactional
    void 이미지_저장하기() throws IOException {
        //given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "bear.jpg",
                "jpg",
                new FileInputStream("bear.jpg")
        );

        //when, then
        assertThatCode(() -> commentImageService.saveImages(getComment().getId(), List.of(image)))
                .doesNotThrowAnyException();


        // 더미 이미지 삭제
        for (CommentImage commentImage : commentImageRepository.findAll()) {
            System.out.println(commentImage.getImageUrl());
            String imageUrl = commentImage.getImageUrl();
            cleanUp(imageUrl);
        }

    }

    @Test
    @Transactional
    void 이미지_삭제하기() throws IOException {
        //given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "bear.jpg",
                "jpg",
                new FileInputStream("bear.jpg")
        );

        final Long commentId = getComment().getId();
        commentImageService.saveImages(commentId, List.of(image));

        //then
        assertThatCode(() -> commentImageService.deleteImages(commentId))
                .doesNotThrowAnyException();

        assertEquals(0, getComment().getCommentImages().size());

    }

    void cleanUp(String imageUrl) {
        // 저장된거 삭제
        System.out.println(imageUrl);
        File deleteFile = new File(imageUrl);
        deleteFile.delete();
    }


}
