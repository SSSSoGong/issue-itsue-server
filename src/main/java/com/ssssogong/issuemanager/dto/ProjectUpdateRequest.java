package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectUpdateRequest {

    private String name;
    private String subject;
}
