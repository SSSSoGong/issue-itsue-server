package com.ssssogong.issuemanager.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    @Test
    void 코멘트_수정() {
        //given
        Comment comment = Comment.builder()
                .content("수정 전")
                .build();

        //when
        String updateContent = "수정 후";
        comment.update(updateContent);

        //then
        assertEquals(updateContent, comment.getContent());
    }
}