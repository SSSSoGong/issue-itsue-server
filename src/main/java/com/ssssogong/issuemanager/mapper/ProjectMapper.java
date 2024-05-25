package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponseDto;
import com.ssssogong.issuemanager.dto.ProjectIdResponseDto;

public class ProjectMapper {

    public static ProjectDetailsResponseDto toProjectDetailsResponse(final Project project) {
        return ProjectDetailsResponseDto.builder()
                .projectId(project.getId())
                .name(project.getName())
                .subject(project.getSubject())
                .createdAt(project.getCreatedAt().toString())
                .adminId(project.getAdmin().getAccountId())
                .adminName(project.getAdmin().getUsername())
                .build();
    }

    public static ProjectIdResponseDto toProjectIdResponse(final long projectId) {
        return ProjectIdResponseDto.builder()
                .projectId(projectId)
                .build();
    }
}
