package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.dto.ProjectUserResponse;
import com.ssssogong.issuemanager.dto.UserProjectAssociationResponse;
import com.ssssogong.issuemanager.dto.UserProjectSummaryResponse;
import java.util.List;
import java.util.Objects;

public class UserProjectMapper {

    public static List<ProjectUserResponse> toProjectUserResponse(final List<UserProject> userProjects) {
        return userProjects.stream()
                .map(each -> ProjectUserResponse.builder()
                        .accountId(each.getUser().getAccountId())
                        .username(each.getUser().getUsername())
                        .role(each.getRole().getRoleName())
                        .build()
                ).toList();
    }

    public static List<UserProjectSummaryResponse> toUserProjectSummaryResponse(final List<UserProject> userProjects) {
        return userProjects.stream()
                .map(each -> UserProjectSummaryResponse.builder()
                        .projectId(each.getProject().getId())
                        .name(each.getProject().getName())
                        .createdAt(each.getProject().getCreatedAt().toString())
                        .isFavorite(each.isFavorite())
                        .build()
                ).toList();
    }

    public static UserProjectAssociationResponse toUserProjectAssociationResponse(final UserProject userProject) {
        return UserProjectAssociationResponse.builder()
                .role(userProject.getRole().getRoleName())
                .createdAt(userProject.getProject().getCreatedAt().toString())
                .accessTime(Objects.isNull(userProject.getAccessTime()) ? null : userProject.getAccessTime().toString())
                .build();
    }
}
