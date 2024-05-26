package com.ssssogong.issuemanager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.dto.DailyStatisticsResponseDto;
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
        final List<DailyStatisticsResponseDto> dailyStatistics = issueStatisticsService.getDailyStatistics(null);

        // then
        assertThat(dailyStatistics).hasSize(7); //총 7일간의 이슈 발생 기록
        for (DailyStatisticsResponseDto dto : dailyStatistics) {
            if (dto.getDate().equals(LocalDateTime.now().toLocalDate().format(formatter))) {
                assertThat(dto.getIssueCount()).isEqualTo(todayIssueCount); //오늘 발생한 이슈가 10개
            } else {
                assertThat(dto.getIssueCount()).isZero(); //그 외엔 이슈가 발생하지 않음
            }
        }
    }
}
