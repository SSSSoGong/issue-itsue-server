package com.ssssogong.issuemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ProjectTest {

    @Test
    void 프로젝트_정보를_수정한다() {
        // given
        final Project project = Project.builder()
                .name("기막힌 프로젝트")
                .subject("이런이런 프로젝트예요")
                .build();

        // when
        final String updatedProjectName = "어메이징 프로젝트";
        final String updatedProjectSubject = "프로젝트 내용이 바꼈어요";
        project.update(updatedProjectName, updatedProjectSubject);

        // then
        assertThat(project.getName()).isEqualTo(updatedProjectName);
        assertThat(project.getSubject()).isEqualTo(updatedProjectSubject);
    }

    @Test
    void 프로젝트_정보를_수정하는데_프로젝트_이름이_빈값이면_현재_프로젝트_이름을_유지한다() {
        // given
        final String originalProjectName = "기막힌 프로젝트";
        final Project project = Project.builder()
                .name(originalProjectName)
                .subject("이런이런 프로젝트예요")
                .build();

        // when
        final String updatedProjectSubject = "프로젝트 내용이 바꼈어요";
        project.update("", updatedProjectSubject);

        // then
        assertThat(project.getName()).isEqualTo(originalProjectName);
        assertThat(project.getSubject()).isEqualTo(updatedProjectSubject);
    }

    @Test
    void 프로젝트_정보를_수정하는데_프로젝트_설명이_빈값이면_현재_프로젝트_설명을_유지한다() {
        // given
        final String originalProjectSubject = "이런이런 프로젝트예요";
        final Project project = Project.builder()
                .name("기막힌 프로젝트")
                .subject(originalProjectSubject)
                .build();

        // when
        final String updatedProjectName = "어메이징 프로젝트";
        project.update(updatedProjectName, "");

        // then
        assertThat(project.getName()).isEqualTo(updatedProjectName);
        assertThat(project.getSubject()).isEqualTo(originalProjectSubject);
    }
}
