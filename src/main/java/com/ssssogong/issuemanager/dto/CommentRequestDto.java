package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



//@AllArgsConstructor // modelattribute 바인딩 시 필요
@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private Long id;
    private String content;
    private List<MultipartFile> imageFiles;

    private String createdAt;
    private String updatedAt;


}
