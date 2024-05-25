package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectUpdateRequestDto {

    private String name;
    private String subject;
}
