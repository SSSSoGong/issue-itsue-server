package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProjectSummaryResponse {

    private Long projectId;
    private String name;
    private String createdAt;
    private boolean isFavorite;
}
