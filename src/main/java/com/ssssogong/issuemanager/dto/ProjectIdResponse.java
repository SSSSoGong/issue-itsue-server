package com.ssssogong.issuemanager.dto;

import lombok.Getter;

@Getter
public class ProjectIdResponse {

    private Long projectId;

    public ProjectIdResponse(final Long projectId) {
        this.projectId = projectId;
    }
}
