package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProjectSummaryResponse {

    private Long projectId;
    private String name;
    private String createdAt;
    private boolean isFavorite;
}
