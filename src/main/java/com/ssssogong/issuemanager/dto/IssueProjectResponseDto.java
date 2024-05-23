package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssueProjectResponseDto {
    private String title;
    private String priority;
    private String state;
    private String category;
    private String createdAt;
    private String updatedAt;

    @Builder
    public IssueProjectResponseDto(String title, String priority, String state, String category, String createdAt, String updatedAt) {
        this.title = title;
        this.priority = priority;
        this.state = state;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
