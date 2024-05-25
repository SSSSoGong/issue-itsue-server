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
    private String reporter;
    private String fixer;
    private String assignee;
    private String createdAt;
    private String updatedAt;

    @Builder
    public IssueProjectResponseDto(String title, String priority, String state, String category, String reporter, String fixer, String assignee, String createdAt, String updatedAt) {
        this.title = title;
        this.priority = priority;
        this.state = state;
        this.category = category;
        this.reporter = reporter;
        this.fixer = fixer;
        this.assignee = assignee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
