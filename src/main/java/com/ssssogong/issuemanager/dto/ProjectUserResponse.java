package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectUserResponse {

    private String accountId;
    private String username;
    private String role;
}
