package com.ssssogong.issuemanager.dto;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IssueResponseDto {

    private String title;
    private String description;
    private String priority;
    private String state;
    private String category;
    private String reporter;
    private String reportedDate;
    private List<String> imageUrls;

    @Builder
    public IssueResponseDto(String title, String description, String priority, String state, String category, String reporter, String reportedDate, List<String> imageUrls) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.state = state;
        this.category = category;
        this.reporter = reporter;
        this.reportedDate = reportedDate;
        this.imageUrls = imageUrls;
    }
}