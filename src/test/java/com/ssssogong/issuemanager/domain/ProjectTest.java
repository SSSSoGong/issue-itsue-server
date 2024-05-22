package com.ssssogong.issuemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
}
