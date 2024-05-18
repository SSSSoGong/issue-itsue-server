package com.ssssogong.issuemanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueSaveRequestDto {

    private Long projectId;
    private String title;
    private String description;
    private String category;
    private String priority;


}
