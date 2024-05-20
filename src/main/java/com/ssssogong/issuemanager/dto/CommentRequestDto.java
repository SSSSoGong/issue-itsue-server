package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



@NoArgsConstructor
@Getter
@Setter
public class CommentRequestDto {

    private String content;
    private List<String> imageUrls;

    private String createdAt;
    private String updatedAt;


}
