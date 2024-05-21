package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssueStateUpdateRequestDto {

    private String state;
    private String assignee;

    @Builder
    public IssueStateUpdateRequestDto(String state, String assignee) {
        this.state = state;
        this.assignee = assignee;
    }
}
