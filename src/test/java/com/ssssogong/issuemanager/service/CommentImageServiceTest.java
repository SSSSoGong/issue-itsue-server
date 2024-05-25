package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.CommentRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

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
    void 이미지_저장하기() throws IOException {
        //given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "bear.jpg",
                "jpg",
                new FileInputStream("bear.jpg")
        );

        //when, then
        assertThatCode(() ->commentImageService.saveImages(getComment().getId(), List.of(image)))
                .doesNotThrowAnyException();


        // 더미 이미지 삭제
        for(CommentImage commentImage: commentImageRepository.findAll()) {
            String imageUrl = commentImage.getImageUrl();
            cleanUp(imageUrl);
        }

    }

    @Test
    void 이미지_삭제하기() throws IOException {
        //given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "bear.jpg",
                "jpg",
                new FileInputStream("bear.jpg")
        );

        CommentImage commentImage = CommentImage.builder()
                .imageUrl("bear.jpg")
                .comment(getComment())
                .build();

        commentImageRepository.save(commentImage);

        //then
        assertThatCode(() -> commentImageService.deleteImages(getComment().getId()))
                .doesNotThrowAnyException();

        assertEquals(0, getComment().getCommentImages().size());

    }

    void cleanUp(String imageUrl) {
        // 저장된거 삭제
        File deleteFile = new File(imageUrl);
        deleteFile.delete();
    }


}