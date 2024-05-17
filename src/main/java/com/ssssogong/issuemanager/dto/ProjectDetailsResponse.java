package com.ssssogong.issuemanager.dto;

import com.ssssogong.issuemanager.domain.Project;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProjectDetailsResponse {

    private Long projectId;
    private String name;
    private String subject;
    private String createdAt;
    private String adminId;
    private String adminName;

    public static ProjectDetailsResponse from(final Project project) {
        return new ProjectDetailsResponse(
                project.getId(),
                project.getName(),
                project.getSubject(),
                project.getCreatedAt().toString(),
                project.getAdmin().getAccountId(),
                project.getAdmin().getUsername()
        );
    }
}
