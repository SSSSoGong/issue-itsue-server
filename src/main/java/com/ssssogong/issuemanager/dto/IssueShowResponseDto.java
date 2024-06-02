package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IssueShowResponseDto {

    private String title;
    private String description;
    private String priority;
    private String state;
    private String category;
    private String reporter;
    private String reportedDate;
    private List<String> imageUrls;
    private String assignee;

    @Builder
    public IssueShowResponseDto(String title, String description, String priority, String state, String category, String reporter, String reportedDate, List<String> imageUrls, String assignee) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = state;
        this.category = category;
        this.reporter = reporter;
        this.reportedDate = reportedDate;
        this.imageUrls = imageUrls;
        this.assignee = assignee;
    }
}