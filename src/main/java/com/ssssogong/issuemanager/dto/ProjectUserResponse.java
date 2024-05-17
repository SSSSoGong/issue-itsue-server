package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectUserResponse {

    private String accountId;
    private String username;
    private String role;
}
