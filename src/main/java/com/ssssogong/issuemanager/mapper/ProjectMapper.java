package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectIdResponse;

public class ProjectMapper {

    public static ProjectDetailsResponse toProjectDetailsResponse(final Project project) {
        return ProjectDetailsResponse.builder()
                .projectId(project.getId())
                .name(project.getName())
                .subject(project.getSubject())
                .createdAt(project.getCreatedAt().toString())
                .adminId(project.getAdmin().getAccountId())
                .adminName(project.getAdmin().getUsername())
                .build();
    }

    public static ProjectIdResponse toProjectIdResponse(final long projectId) {
        return ProjectIdResponse.builder()
                .projectId(projectId)
                .build();
    }
}
