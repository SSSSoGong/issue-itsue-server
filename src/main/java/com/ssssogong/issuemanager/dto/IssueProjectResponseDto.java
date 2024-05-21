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
    private String state;
    private String createdAt;
    private String updatedAt;

    @Builder
    public IssueProjectResponseDto(String title, String state, String createdAt, String updatedAt) {
        this.title = title;
        this.state = state;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
