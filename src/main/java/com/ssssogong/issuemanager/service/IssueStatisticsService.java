package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.dto.CategoryStatisticsResponseDto;
import com.ssssogong.issuemanager.dto.DateStatisticsResponseDto;
import com.ssssogong.issuemanager.dto.PriorityStatisticsResponseDto;
import com.ssssogong.issuemanager.repository.IssueRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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
@Transactional(readOnly = true)
public class IssueStatisticsService {

    private static final Integer DEFAULT_DAILY_PERIOD = 7;
    private static final String DAILY_FORMAT = "yyyy-MM-dd";
    private static final Integer DEFAULT_MONTHLY_PERIOD = 6;
    private static final String MONTHLY_FORMAT = "yyyy-MM";

    private final IssueRepository issueRepository;

    public List<DateStatisticsResponseDto> getDailyStatistics(Long projectId, Integer period) {
        if (Objects.isNull(period)) {
            period = DEFAULT_DAILY_PERIOD;
        }
        LocalDateTime nDaysAgo = LocalDateTime.now().minus(period - 1, ChronoUnit.DAYS);
        final List<Issue> issues = issueRepository.findIssuesCreatedSinceAndByProjectId(projectId, nDaysAgo);

        List<LocalDate> recentDays = nDaysAgo.toLocalDate().datesUntil(LocalDate.now().plusDays(1))
                .toList();

        //일별로 분류해서 이슈 발생 횟수를 센다
        Map<LocalDate, Long> issuesByDate = issues.stream()
                .collect(Collectors.groupingBy(issue -> issue.getCreatedAt().toLocalDate(), Collectors.counting()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DAILY_FORMAT);
        return recentDays.stream()
                .map(date -> new DateStatisticsResponseDto(date.format(formatter),
                        issuesByDate.getOrDefault(date, 0L).intValue()))
                .toList();
    }

    public List<DateStatisticsResponseDto> getMonthlyStatistics(Long projectId, Integer period) {
        if (Objects.isNull(period)) {
            period = DEFAULT_MONTHLY_PERIOD;
        }
        LocalDateTime nMonthsAgo = LocalDateTime.now().minusMonths(period - 1).withDayOfMonth(1);
        final List<Issue> issues = issueRepository.findIssuesCreatedSinceAndByProjectId(projectId, nMonthsAgo);
        List<LocalDate> recentMonths = nMonthsAgo.toLocalDate().datesUntil(LocalDate.now().plusDays(1), Period.ofMonths(1))
                .toList();

        //월별로 분류해서 이슈 발생 횟수를 센다
        Map<LocalDate, Long> issuesByMonth = issues.stream()
                .collect(Collectors.groupingBy(issue -> issue.getCreatedAt().toLocalDate().withDayOfMonth(1),
                        Collectors.counting()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MONTHLY_FORMAT);
        return recentMonths.stream()
                .map(month -> new DateStatisticsResponseDto(month.format(formatter),
                        issuesByMonth.getOrDefault(month, 0L).intValue()))
                .toList();
    }

    public List<CategoryStatisticsResponseDto> getCategoryStatistics(Long projectId) {
        final List<Issue> issues = issueRepository.findByProjectId(projectId);
        Map<Category, Long> issuesByCategory = issues.stream()
                .collect(Collectors.groupingBy(Issue::getCategory, Collectors.counting()));

        //이슈가 0개인 카테고리 추가
        for (Category category : Category.values()) {
            issuesByCategory.putIfAbsent(category, 0L);
        }

        return issuesByCategory.entrySet().stream()
                .map(entry -> new CategoryStatisticsResponseDto(entry.getKey().name(), entry.getValue().intValue()))
                .toList();
    }

    public List<PriorityStatisticsResponseDto> getPriorityStatistics(Long projectId) {
        final List<Issue> issues = issueRepository.findByProjectId(projectId);
        Map<Priority, Long> issuesByPriority = issues.stream()
                .collect(Collectors.groupingBy(Issue::getPriority, Collectors.counting()));

        //이슈가 0개인 우선순위 추가
        for (Priority priority : Priority.values()) {
            issuesByPriority.putIfAbsent(priority, 0L);
        }

        return issuesByPriority.entrySet().stream()
                .map(entry -> new PriorityStatisticsResponseDto(entry.getKey().name(), entry.getValue().intValue()))
                .toList();
    }
}
