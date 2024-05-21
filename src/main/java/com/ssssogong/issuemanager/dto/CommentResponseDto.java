package com.ssssogong.issuemanager.dto;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Setter
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
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();

        this.imageUrls = comment.getCommentImages().stream()
                .map(CommentImage::getImageUrls)
                .collect(Collectors.toList());
    }
}


