package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.DailyStatisticsResponseDto;
import com.ssssogong.issuemanager.service.IssueStatisticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issue-statistics")
public class IssueStatisticsController {

    private final IssueStatisticsService issueStatisticsService;

    @GetMapping("/daily")
    public ResponseEntity<List<DailyStatisticsResponseDto>> getDailyStatistics(
            @RequestParam(name = "period", required = false) final Integer period
    ) {
        final List<DailyStatisticsResponseDto> response = issueStatisticsService.getDailyStatistics(period);
        return ResponseEntity.ok(response);
    }
}
