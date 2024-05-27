package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriorityStatisticsResponseDto {

    private String category;
    private int issueCount;
}
