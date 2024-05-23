package com.ssssogong.issuemanager.dto;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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


