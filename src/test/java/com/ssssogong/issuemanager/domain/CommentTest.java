package com.ssssogong.issuemanager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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