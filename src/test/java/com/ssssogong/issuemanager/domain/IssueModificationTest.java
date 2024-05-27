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
public class IssueModificationTest {

    @Test
    void 이슈_상태_변경() {
        // given
        final State from = State.NEW;
        final State to = State.ASSIGNED;
        final Issue issue = Issue.builder()
                .title("엄청난 이슈")
                .description("엄청난 이슈입니다")
                .priority(Priority.MAJOR)
                .state(from)
                .category(Category.REFACTORING)
                .build();

        // when
        issue.setState(to);
        final IssueModification issueModification = IssueModification.builder()
                .from(from)
                .to(to)
                .issue(issue)
                .build();

        // then
        assertThat(issueModification.getIssue()).isEqualTo(issue);
        assertThat(issueModification.getFrom()).isEqualTo(from);
        assertThat(issueModification.getTo()).isEqualTo(to);
    }
}
