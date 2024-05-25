package com.ssssogong.issuemanager.repository;


import com.ssssogong.issuemanager.domain.IssueModification;
import com.ssssogong.issuemanager.domain.enumeration.State;
import jakarta.transaction.Transactional;
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
public class IssueModificationRepositoryTest {

    @Autowired
    private IssueModificationRepository issueModificationRepository;

    @Test
    void 이슈수정_저장_조회() {
        // given
        final IssueModification issueModification1 = IssueModification.builder()
                .id(1L)
                .from(State.NEW)
                .to(State.ASSIGNED)
                .build();

        final IssueModification issueModification2 = IssueModification.builder()
                .id(2L)
                .from(State.ASSIGNED)
                .to(State.FIXED)
                .build();

        issueModificationRepository.save(issueModification1);
        issueModificationRepository.save(issueModification2);

        // when
        List<IssueModification> issueModifications = issueModificationRepository.findAll();

        // then
        assertThat(issueModifications).hasSize(2);
        assertThat(issueModifications.get(0).getId()).isEqualTo(1L);
        assertThat(issueModifications.get(0).getFrom()).isEqualTo(State.NEW);
        assertThat(issueModifications.get(0).getTo()).isEqualTo(State.ASSIGNED);
        assertThat(issueModifications.get(1).getId()).isEqualTo(2L);
        assertThat(issueModifications.get(1).getFrom()).isEqualTo(State.ASSIGNED);
        assertThat(issueModifications.get(1).getTo()).isEqualTo(State.FIXED);
    }

    @Test
    void 이슈수정_삭제() {
        // given
        final IssueModification issueModification = IssueModification.builder()
                .id(1L)
                .from(State.NEW)
                .to(State.ASSIGNED)
                .build();
        issueModificationRepository.save(issueModification);

        // when
        issueModificationRepository.deleteById(1L);
        List<IssueModification> issueModifications = issueModificationRepository.findAll();

        // then
        assertThat(issueModifications).isEmpty();
    }
}
