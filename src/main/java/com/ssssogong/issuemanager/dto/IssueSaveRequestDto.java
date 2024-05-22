package com.ssssogong.issuemanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IssueSaveRequestDto {

    private String title;
    private String description;
    private String category;
    private String priority;

    @Builder
    public IssueSaveRequestDto(String title, String description, String category, String priority) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
    }
}
