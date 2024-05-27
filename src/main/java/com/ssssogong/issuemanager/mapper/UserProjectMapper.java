package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.dto.ProjectUserResponseDto;
import com.ssssogong.issuemanager.dto.UserProjectAssociationResponseDto;
import com.ssssogong.issuemanager.dto.UserProjectSummaryResponseDto;

import java.util.List;
import java.util.Objects;

public class UserProjectMapper {

    public static List<ProjectUserResponseDto> toProjectUserResponse(final List<UserProject> userProjects) {
        return userProjects.stream()
                .map(each -> ProjectUserResponseDto.builder()
                        .accountId(each.getUser().getAccountId())
                        .username(each.getUser().getUsername())
                        .role(each.getRole().getRoleName())
                        .build()
                ).toList();
    }

    public static List<UserProjectSummaryResponseDto> toUserProjectSummaryResponse(final List<UserProject> userProjects) {
        return userProjects.stream()
                .map(each -> UserProjectSummaryResponseDto.builder()
                        .projectId(each.getProject().getId())
                        .name(each.getProject().getName())
                        .createdAt(each.getProject().getCreatedAt().toString())
                        .isFavorite(each.isFavorite())
                        .build()
                ).toList();
    }

    public static UserProjectAssociationResponseDto toUserProjectAssociationResponse(final UserProject userProject) {
        return UserProjectAssociationResponseDto.builder()
                .role(userProject.getRole().getRoleName())
                .createdAt(userProject.getProject().getCreatedAt().toString())
                .accessTime(Objects.isNull(userProject.getAccessTime()) ? null : userProject.getAccessTime().toString())
                .build();
    }
}
