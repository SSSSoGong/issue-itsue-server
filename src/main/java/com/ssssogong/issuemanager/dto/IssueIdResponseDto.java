package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueIdResponseDto {

    private Long issueId;

    @Builder
    public IssueIdResponseDto(Long issueId) {
        this.issueId = issueId;
    }
}
