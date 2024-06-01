package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.IssueImage;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@Sql("/truncate.sql")
public class IssueImageRepositoryTest {

    @Autowired
    private IssueImageRepository issueImageRepository;

    @Test
    void 이슈이미지_저장_조회() {
        // given
        final IssueImage issueImage = IssueImage.builder()
                .id(1L)
                .imageUrl("이미지 경로")
                .build();

        // when
        issueImageRepository.save(issueImage);
        IssueImage findIssueImage = issueImageRepository.findById(issueImage.getId()).orElse(null);

        // then
        assertThat(findIssueImage).isNotNull();
        assertThat(findIssueImage.getId()).isEqualTo(issueImage.getId());
        assertThat(findIssueImage.getImageUrl()).isEqualTo(issueImage.getImageUrl());
    }

    @Test
    void 이슈이미지_삭제(){
        // given
        final IssueImage issueImage = IssueImage.builder()
                .id(1L)
                .imageUrl("이미지 경로")
                .build();
        issueImageRepository.save(issueImage);

        // when
        issueImageRepository.deleteById(issueImage.getId());
        IssueImage findIssueImage = issueImageRepository.findById(issueImage.getId()).orElse(null);

        // then
        assertThat(findIssueImage).isNull();
    }
}
