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
public class IssueTest {

    @Test
    void 이슈_정보_수정() {
        // given
        final Issue issue = Issue.builder()
                .title("엄청난 이슈")
                .description("엄청난 이슈입니다")
                .priority(Priority.MAJOR)
                .state(State.NEW)
                .category(Category.REFACTORING)
                .build();

        // when
        final String title = "대단한 이슈";
        final String description = "대단한 이슈입니다";
        issue.update(title, description, null);

        // then
        assertThat(issue.getTitle()).isEqualTo(title);
        assertThat(issue.getDescription()).isEqualTo(description);
    }

    @Test
    void 이슈_정보를_수정하는데_이슈_타이틀이_빈값이면_현재_타이틀을_유지한다() {
        // given
        final String originalTitle = "엄청난 이슈";
        final Issue issue = Issue.builder()
                .title(originalTitle)
                .description("엄청난 이슈입니다")
                .priority(Priority.MAJOR)
                .state(State.NEW)
                .category(Category.REFACTORING)
                .build();

        // when
        final String title = "";
        final String description = "대단한 이슈입니다";
        issue.update(title, description, null);

        // then
        assertThat(issue.getTitle()).isEqualTo(originalTitle);
        assertThat(issue.getDescription()).isEqualTo(description);
    }

    @Test
    void 이슈_설명을_수정하는데_이슈_설명이_빈값이면_현재_설명을_유지한다() {
        // given
        final String originalDescription = "엄청난 이슈입니다";
        final Issue issue = Issue.builder()
                .title("엄청난 이슈")
                .description(originalDescription)
                .priority(Priority.MAJOR)
                .state(State.NEW)
                .category(Category.REFACTORING)
                .build();

        // when
        final String title = "대단한 이슈";
        final String description = "";
        issue.update(title, description, null);

        // then
        assertThat(issue.getTitle()).isEqualTo(title);
        assertThat(issue.getDescription()).isEqualTo(originalDescription);
    }

    @Test
    void 이슈_우선순위를_수정하는데_우선순위가_빈값이면_현재_우선순위를_유지한다() {
        // given
        final Priority priority = Priority.MAJOR;
        final Issue issue = Issue.builder()
                .title("엄청난 이슈")
                .description("엄청난 이슈입니다")
                .priority(priority)
                .state(State.NEW)
                .category(Category.REFACTORING)
                .build();

        // when
        final String title = "대단한 이슈";
        final String description = "대단한 이슈입니다";
        issue.update(title, description, null);

        // then
        assertThat(issue.getTitle()).isEqualTo(title);
        assertThat(issue.getDescription()).isEqualTo(description);
        assertThat(issue.getPriority()).isEqualTo(priority);
    }

    @Test
    void 이슈_상태를_수정하는데_상태가_빈값이면_현재_상태를_유지한다() {
        // given
        final State state = State.NEW;
        final Issue issue = Issue.builder()
                .title("엄청난 이슈")
                .description("엄청난 이슈입니다")
                .priority(Priority.MAJOR)
                .state(state)
                .category(Category.REFACTORING)
                .build();

        // when
        issue.update(null);

        // then
        assertThat(issue.getState()).isEqualTo(state);
    }
}
