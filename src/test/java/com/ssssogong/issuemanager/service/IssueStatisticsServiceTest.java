package com.ssssogong.issuemanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.dto.CategoryStatisticsResponseDto;
import com.ssssogong.issuemanager.dto.DateStatisticsResponseDto;
import com.ssssogong.issuemanager.dto.PriorityStatisticsResponseDto;
import com.ssssogong.issuemanager.repository.IssueRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class IssueStatisticsServiceTest {

    @Autowired
    private IssueStatisticsService issueStatisticsService;
    @Autowired
    private IssueRepository issueRepository;

    @Test
    void 최근_7일간의_날짜별_이슈_발생_횟수_통계를_조회한다() {
        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final int todayIssueCount = 10;
        List<Issue> setUp = new ArrayList<>();

        for (int i = 0; i < todayIssueCount; i++) {
            setUp.add(Issue.builder().build());
        }
        issueRepository.saveAll(setUp);

        // when
        final List<DateStatisticsResponseDto> dailyStatistics = issueStatisticsService.getDailyStatistics(null);

        // then
        assertThat(dailyStatistics).hasSize(7); //총 7일간의 이슈 발생 기록
        for (DateStatisticsResponseDto dto : dailyStatistics) {
            if (dto.getDate().equals(LocalDateTime.now().toLocalDate().format(formatter))) {
                assertThat(dto.getIssueCount()).isEqualTo(todayIssueCount); //오늘 발생한 이슈가 10개
            } else {
                assertThat(dto.getIssueCount()).isZero(); //그 외엔 이슈가 발생하지 않음
            }
        }
    }

    @Test
    void 최근_6개월간의_월별_이슈_발생_횟수_통계를_조회한다() {
        // given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        final int thisMonthIssueCount = 10;
        List<Issue> setUp = new ArrayList<>();

        for (int i = 0; i < thisMonthIssueCount; i++) {
            setUp.add(Issue.builder().build());
        }
        issueRepository.saveAll(setUp);

        // when
        final List<DateStatisticsResponseDto> monthlyStatistics = issueStatisticsService.getMonthlyStatistics(null);

        // then
        assertThat(monthlyStatistics).hasSize(6); //총 6개월간의 이슈 발생 기록
        for (DateStatisticsResponseDto dto : monthlyStatistics) {
            if (dto.getDate().equals(LocalDateTime.now().toLocalDate().format(formatter))) {
                assertThat(dto.getIssueCount()).isEqualTo(thisMonthIssueCount); //이번달 발생한 이슈가 10개
            } else {
                assertThat(dto.getIssueCount()).isZero(); //그 외엔 이슈가 발생하지 않음
            }
        }
    }

    @Test
    void 카테고리별_이슈_통계를_조회한다() {
        // given
        List<Issue> setUp = new ArrayList<>();

        final Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            setUp.add(Issue.builder().category(categories[i]).build());
        }
        issueRepository.saveAll(setUp);

        // when
        final List<CategoryStatisticsResponseDto> categoryStatistics = issueStatisticsService.getCategoryStatistics();

        // then
        assertThat(categoryStatistics).hasSize(categories.length); //카테고리 개수만큼
        for (CategoryStatisticsResponseDto dto : categoryStatistics) {
            assertThat(dto.getIssueCount()).isOne();
        }
    }

    @Test
    void 우선순위별_이슈_통계를_조회한다() {
        // given
        List<Issue> setUp = new ArrayList<>();

        final Priority[] priorities = Priority.values();
        for (int i = 0; i < priorities.length; i++) {
            setUp.add(Issue.builder().priority(priorities[i]).build());
        }
        issueRepository.saveAll(setUp);

        // when
        final List<PriorityStatisticsResponseDto> priorityStatistics = issueStatisticsService.getPriorityStatistics();

        // then
        assertThat(priorityStatistics).hasSize(priorities.length); //우선순위 개수만큼
        for (PriorityStatisticsResponseDto dto : priorityStatistics) {
            assertThat(dto.getIssueCount()).isOne();
        }
    }
}
