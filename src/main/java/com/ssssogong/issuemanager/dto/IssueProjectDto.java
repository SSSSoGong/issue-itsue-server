package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueProjectDto {
    private String title;
    private String state;
    private String createdAt;
    private String updatedAt;
}
