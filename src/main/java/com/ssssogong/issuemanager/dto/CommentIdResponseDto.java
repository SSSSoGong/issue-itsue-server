package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CommentIdResponseDto {
    private Long commentId;

    public CommentIdResponseDto(Long id) {
        this.commentId = id;
    }
}
