package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueUpdateRequestDto {

    private String title;
    private String description;
    private String priority;
}
