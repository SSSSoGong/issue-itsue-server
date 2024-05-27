package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private List<String> imageUrls;
    private String writerId;
    private String writerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


