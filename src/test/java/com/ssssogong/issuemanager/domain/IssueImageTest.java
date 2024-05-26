package com.ssssogong.issuemanager.domain;


import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class IssueImageTest {

    @Test
    void 이슈_이미지_저장() {
        // given
        final Issue issue = Issue.builder()
                .title("엄청난 이슈")
                .description("엄청난 이슈입니다")
                .priority(Priority.MAJOR)
                .state(State.NEW)
                .category(Category.REFACTORING)
                .build();

        final IssueImage issueImage = IssueImage.builder()
                .imageUrl("멋있는 이미지 경로")
                .build();

        // when
        issueImage.setIssue(issue);

        // then
        assertThat(issue).isEqualTo(issueImage.getIssue());
        assertThat(issue.getIssueImages().get(0)).isEqualTo(issueImage);
        assertThat(issue.getIssueImages().size()).isEqualTo(1);
    }
}
