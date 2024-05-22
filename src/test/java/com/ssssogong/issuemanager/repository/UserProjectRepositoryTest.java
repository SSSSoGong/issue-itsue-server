package com.ssssogong.issuemanager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/truncate.sql")
class UserProjectRepositoryTest {

    @Autowired
    private UserProjectRepository userProjectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void 한_유저라도_이미_프로젝트에_참여했다면_true를_반환한다() {
        // given
        final Project project = projectRepository.save(Project.builder().name("project").build());
        final User user1 = userRepository.save(User.builder().accountId("user1").build());
        final User user2 = userRepository.save(User.builder().accountId("user2").build());
        userProjectRepository.save(UserProject.builder().project(project).user(user1).build());

        // when
        final boolean exists = userProjectRepository.existsByProjectIdAndAccountIdIn(
                project.getId(),
                List.of(user1.getAccountId(), user2.getAccountId())
        );

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 모든_유저가_프로젝트에_참여하지_않았다면_false를_반환한다() {
        // given
        final Project project = projectRepository.save(Project.builder().name("project").build());
        final User user1 = userRepository.save(User.builder().accountId("user1").build());
        final User user2 = userRepository.save(User.builder().accountId("user2").build());
        userProjectRepository.save(UserProject.builder().project(project).build());

        // when
        final boolean exists = userProjectRepository.existsByProjectIdAndAccountIdIn(
                project.getId(),
                List.of(user1.getAccountId(), user2.getAccountId())
        );

        // then
        assertThat(exists).isFalse();
    }
}
