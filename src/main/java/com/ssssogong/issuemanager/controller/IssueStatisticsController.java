package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.CategoryStatisticsResponseDto;
import com.ssssogong.issuemanager.dto.DateStatisticsResponseDto;
import com.ssssogong.issuemanager.dto.PriorityStatisticsResponseDto;
import com.ssssogong.issuemanager.service.IssueStatisticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/issue-statistics")
public class IssueStatisticsController {

    private final IssueStatisticsService issueStatisticsService;

    @GetMapping("/daily")
    public ResponseEntity<List<DateStatisticsResponseDto>> getDailyStatistics(
            @PathVariable(name = "projectId") final Long projectId,
            @RequestParam(name = "period", required = false) final Integer period
    ) {
        final List<DateStatisticsResponseDto> response = issueStatisticsService.getDailyStatistics(projectId, period);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<DateStatisticsResponseDto>> getMonthlyStatistics(
            @PathVariable(name = "projectId") final Long projectId,
            @RequestParam(name = "period", required = false) final Integer period
    ) {
        final List<DateStatisticsResponseDto> response = issueStatisticsService.getMonthlyStatistics(projectId, period);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryStatisticsResponseDto>> getCategoryStatistics(
            @PathVariable(name = "projectId") final Long projectId
    ) {
        List<CategoryStatisticsResponseDto> response = issueStatisticsService.getCategoryStatistics(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/priority")
    public ResponseEntity<List<PriorityStatisticsResponseDto>> getPriorityStatistics(
            @PathVariable(name = "projectId") final Long projectId
    ) {
        List<PriorityStatisticsResponseDto> response = issueStatisticsService.getPriorityStatistics(projectId);
        return ResponseEntity.ok(response);
    }
}
