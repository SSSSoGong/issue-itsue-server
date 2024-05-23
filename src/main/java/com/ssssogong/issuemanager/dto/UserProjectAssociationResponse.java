package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProjectAssociationResponse {

    private String role;
    private String createdAt;
    private String accessTime;
}
