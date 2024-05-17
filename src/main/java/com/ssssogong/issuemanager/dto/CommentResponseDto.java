package com.ssssogong.issuemanager.dto;

import com.ssssogong.issuemanager.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private List<String> imageUrls;
    private String writerId;
    private String writerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writerId = comment.getWriter().getAccountId();
        this.writerName = comment.getWriter().getUsername();
        this.imageUrls = comment.getImageUrls();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

}
