package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailsResponseDto {

    private Long projectId;
    private String name;
    private String subject;
    private String createdAt;
    private String adminId;
    private String adminName;
}
