package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserResponseDto {

    private String accountId;
    private String username;
    private String role;
}
