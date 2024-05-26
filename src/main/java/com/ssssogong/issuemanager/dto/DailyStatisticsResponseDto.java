package com.ssssogong.issuemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyStatisticsResponseDto {

    private String date;
    private Integer issueCount;
}
