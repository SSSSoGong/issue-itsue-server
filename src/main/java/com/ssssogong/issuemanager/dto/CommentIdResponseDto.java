package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentIdResponseDto {
    private Long commentId;

    public CommentIdResponseDto(Long id) {
        this.commentId = id;
    }
}
