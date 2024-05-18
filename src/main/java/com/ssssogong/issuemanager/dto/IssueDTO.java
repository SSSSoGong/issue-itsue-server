package com.ssssogong.issuemanager.dto;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IssueDTO {

    private String title;
    private String description;
    private String priority;
    private String state;
    private String category;
    private String reporter;
    private String reportedDate;

    @Builder
    public IssueDTO(String title, String description, Priority priority, State state, Category category, User reporter, LocalDateTime reportedDate) {
        this.title = title;
        this.description = description;
        this.priority = priority.toString();
        this.state = state.toString();
        this.category = category.toString();
        this.reporter = reporter.getAccountId();
        this.reportedDate = reportedDate.toString();
    }
}