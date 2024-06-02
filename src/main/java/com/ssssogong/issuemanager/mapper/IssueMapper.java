package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueImage;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.dto.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class IssueMapper {


    public static Issue toIssueFromSaveRequestDto(User reporter, Project project, IssueSaveRequestDto issueSaveRequestDto) {
        return Issue.builder()
                .title(issueSaveRequestDto.getTitle())
                .description(issueSaveRequestDto.getDescription())
                .priority(Priority.valueOf(issueSaveRequestDto.getPriority()))
                .state(State.NEW)
                .category(Category.valueOf(issueSaveRequestDto.getCategory()))
                .reporter(reporter)
                .fixer(null)
                .assignee(null)
                .project(project)
                .comments(new ArrayList<>())
                .issueModifications(new ArrayList<>())
                .issueImages(new ArrayList<>())
                .build();
    }

    public static IssueIdResponseDto toIssueIdResponseDto(Issue issue) {
        return IssueIdResponseDto.builder()
                .issueId(issue.getId())
                .build();
    }

    public static IssueShowResponseDto toIssueShowResponseDto(Issue issue) {
        return IssueShowResponseDto.builder()
                .title(issue.getTitle())
                .description(issue.getDescription())
                .priority(issue.getPriority() != null ? issue.getPriority().toString() : null)
                .state(issue.getState() != null ? issue.getState().toString() : null)
                .category(issue.getCategory() != null ? issue.getCategory().toString() : null)
                .reporter(issue.getReporter() != null ? issue.getReporter().getAccountId() : null)
                .reportedDate(issue.getCreatedAt() != null ? issue.getCreatedAt().toString() : null)
                .imageUrls(issue.getIssueImages() != null ? issue.getIssueImages().stream()
                        .map(IssueImage::getImageUrl)
                        .collect(Collectors.toList()) : null)
                .assignee(issue.getAssignee().getAccountId())
                .build();
    }

    public static IssueProjectResponseDto toIssueProjectResponseDto(Issue issue) {
        return IssueProjectResponseDto.builder()
                .issueId(issue.getId())
                .title(issue.getTitle())
                .priority(issue.getPriority() != null ? issue.getPriority().toString() : null)
                .state(issue.getState() != null ? issue.getState().toString() : null)
                .category(issue.getCategory() != null ? issue.getCategory().toString() : null)
                .reporter(issue.getReporter() != null ? issue.getReporter().getAccountId() : null)
                .fixer(issue.getFixer() != null ? issue.getFixer().getAccountId() : null)
                .assignee(issue.getAssignee() != null ? issue.getAssignee().getAccountId() : null)
                .createdAt(issue.getCreatedAt() != null ? issue.getCreatedAt().toString() : null)
                .updatedAt(issue.getUpdatedAt() != null ? issue.getUpdatedAt().toString() : null)
                .build();
    }
}

