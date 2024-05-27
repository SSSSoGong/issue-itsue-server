package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Issue;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@Sql("/truncate.sql")
public class IssueRepositoryTest {

    @Autowired
    private IssueRepository issueRepository;

    @Test
    void 이슈_저장_조회() {
        // given
        Issue issue_1 = Issue.builder()
                .id(1L)
                .title("대단한 이슈")
                .description("대단한 이슈 입니다")
                .build();

        Issue issue_2 = Issue.builder()
                .id(2L)
                .title("엄청난 이슈")
                .description("엄청난 이슈 입니다")
                .build();

        issueRepository.save(issue_1);
        issueRepository.save(issue_2);

        // when
        List<Issue> issues = issueRepository.findAll();

        // then
        assertThat(issues).isNotEmpty();
        assertThat(issues.size()).isEqualTo(2);
        assertThat(issues.get(0).getId()).isEqualTo(1L);
        assertThat(issues.get(1).getId()).isEqualTo(2L);
        assertThat(issues.get(0).getDescription()).isEqualTo(issue_1.getDescription());
        assertThat(issues.get(1).getDescription()).isEqualTo(issue_2.getDescription());

    }

    @Test
    void 이슈_수정() {
        // given
        Issue issue = Issue.builder()
                .id(1L)
                .title("대단한 이슈")
                .description("대단한 이슈 입니다")
                .build();
        issueRepository.save(issue);

        // when
        final String newTitle = "엄청난 이슈";
        final String newDescription = "엄청난 이슈 입니다";
        issue.setTitle(newTitle);
        issue.setDescription(newDescription);
        issueRepository.save(issue);

        Issue updatedIssue = issueRepository.findById(issue.getId()).orElse(null);
        // then
        assertThat(updatedIssue).isNotNull();
        assertThat(updatedIssue.getTitle()).isEqualTo(newTitle);
        assertThat(updatedIssue.getDescription()).isEqualTo(newDescription);
    }

    @Test
    void 이슈_삭제() {
        // given
        Issue issue = Issue.builder()
                .id(1L)
                .title("대단한 이슈")
                .description("대단한 이슈 입니다")
                .build();
        issueRepository.save(issue);

        // when
        issueRepository.deleteById(issue.getId());
        List<Issue> issues = issueRepository.findAll();

        // then
        assertThat(issues).isEmpty();
    }
}
