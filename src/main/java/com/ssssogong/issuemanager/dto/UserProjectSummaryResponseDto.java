package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectSummaryResponseDto {

    private Long projectId;
    private String name;
    private String createdAt;
    private boolean isFavorite;
}
