package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueStateUpdateRequestDto {

    private String state;
    private String assignee;
}
