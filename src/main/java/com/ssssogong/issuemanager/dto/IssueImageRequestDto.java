package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class IssueImageRequestDto {

    private List<MultipartFile> imageFiles;

    @Builder
    public IssueImageRequestDto(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}