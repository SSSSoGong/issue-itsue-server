package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CommentImageRequestDto {

    private List<MultipartFile> imageFiles;
}
