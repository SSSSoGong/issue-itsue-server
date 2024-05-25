package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectUserResponseDto {

    private String accountId;
    private String username;
    private String role;
}
