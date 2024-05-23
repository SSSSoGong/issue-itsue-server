package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectUserAdditionRequest {

    private String accountId;
    private String role;
}
