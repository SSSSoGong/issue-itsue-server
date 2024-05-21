package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentImageRequestDto {
    private List<MultipartFile> imageFiles;
}
