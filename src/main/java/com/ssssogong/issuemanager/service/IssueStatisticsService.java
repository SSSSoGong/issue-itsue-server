package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.dto.DailyStatisticsResponseDto;
import com.ssssogong.issuemanager.repository.IssueRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class IssueStatisticsService {

    private static final Integer DEFAULT_PERIOD = 7;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final IssueRepository issueRepository;

    @Transactional(readOnly = true)
    public List<DailyStatisticsResponseDto> getDailyStatistics(Integer period) {
        if(Objects.isNull(period)) {
            period = DEFAULT_PERIOD;
        }
        LocalDateTime nDaysAgo = LocalDateTime.now().minus(period, ChronoUnit.DAYS);
        final List<Issue> issues = issueRepository.findIssuesCreatedAfter(nDaysAgo);

        List<LocalDate> recentDays = LocalDate.now().minusDays(DEFAULT_PERIOD-1).datesUntil(LocalDate.now().plusDays(1))
                .toList();

        //일별로 분류해서 이슈 발생 횟수를 센다
        Map<LocalDate, Long> issuesByDate = issues.stream()
                .collect(Collectors.groupingBy(issue -> issue.getCreatedAt().toLocalDate(), Collectors.counting()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return recentDays.stream()
                .map(date -> new DailyStatisticsResponseDto(date.format(formatter),
                        issuesByDate.getOrDefault(date, 0L).intValue()))
                .toList();
    }
}
