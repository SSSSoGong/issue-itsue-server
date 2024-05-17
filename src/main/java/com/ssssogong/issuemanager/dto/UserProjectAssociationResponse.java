package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProjectAssociationResponse {

    private String role;
    private String createdAt;
    private String accessTime;
}
