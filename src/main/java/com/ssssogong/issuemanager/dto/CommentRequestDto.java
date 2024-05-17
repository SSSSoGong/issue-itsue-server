package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private Long id;
    private String content;
    private List<MultipartFile> imageFiles;
    private String writerAccountId;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


}
