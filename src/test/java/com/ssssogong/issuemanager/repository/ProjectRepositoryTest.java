package com.ssssogong.issuemanager.repository;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.Project;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DataJpaTest
@Sql("/truncate.sql")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private IssueRepository issueRepository;

    @Test
    void 연관된_이슈가_존재해도_프로젝트를_삭제할_수_있다() {
        // given
        final Project project = projectRepository.save(Project.builder().name("야심찬 프로젝트").build());
        issueRepository.save(Issue.builder().project(project).title("야심찬 이슈").build());

        // when, then
        assertThatCode(() -> projectRepository.deleteById(project.getId()))
                .doesNotThrowAnyException();
    }
}
