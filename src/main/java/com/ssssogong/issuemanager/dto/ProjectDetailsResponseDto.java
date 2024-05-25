package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailsResponseDto {

    private Long projectId;
    private String name;
    private String subject;
    private String createdAt;
    private String adminId;
    private String adminName;
}
