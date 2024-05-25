package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserRepository userRepository;

    private User getAdmin() {
        final User admin = User.builder()
                .accountId("admin")
                .username("어드민")
                .password("password").build();
        userRepository.save(admin);
        return admin;
    }

    @Test
    void Admin이_프로젝트를_생성한다() {
        //given
        final ProjectCreationRequest projectCreationRequest = ProjectCreationRequest.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();

        //when, then
        assertThatCode(() -> projectService.create(getAdmin().getAccountId(), projectCreationRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void 프로젝트_정보를_조회한다() {
        // given
        final ProjectCreationRequest projectCreationRequest = ProjectCreationRequest.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();
        final User admin = getAdmin();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequest).getProjectId();

        // when
        final ProjectDetailsResponse response = projectService.findById(projectId);

        // then
        assertAll(
                () -> assertThat(response.getProjectId()).isEqualTo(projectId),
                () -> assertThat(response.getAdminId()).isEqualTo(admin.getAccountId()),
                () -> assertThat(response.getAdminName()).isEqualTo(admin.getUsername()),
                () -> assertThat(response.getName()).isEqualTo("야심찬 프로젝트"),
                () -> assertThat(response.getSubject()).isEqualTo("어쩌구 저쩌구 프로젝트입니다."),
                () -> assertThat(response.getCreatedAt()).isNotEmpty()
        );
    }

    @Test
    void 프로젝트_정보를_수정한다() {
        // given
        final ProjectCreationRequest projectCreationRequest = ProjectCreationRequest.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();
        final User admin = getAdmin();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequest).getProjectId();
        final ProjectUpdateRequest projectUpdateRequest = ProjectUpdateRequest.builder()
                .name("야심찬 프로젝트(수정)")
                .subject("어쩌구 저쩌구 프로젝트입니다.(수정)")
                .build();

        // when
        projectService.updateById(projectId, projectUpdateRequest);
        final ProjectDetailsResponse response = projectService.findById(projectId);

        // then
        assertAll(
                () -> assertThat(response.getProjectId()).isEqualTo(projectId),
                () -> assertThat(response.getAdminId()).isEqualTo(admin.getAccountId()),
                () -> assertThat(response.getAdminName()).isEqualTo(admin.getUsername()),
                () -> assertThat(response.getName()).isEqualTo("야심찬 프로젝트(수정)"),
                () -> assertThat(response.getSubject()).isEqualTo("어쩌구 저쩌구 프로젝트입니다.(수정)"),
                () -> assertThat(response.getCreatedAt()).isNotEmpty()
        );
    }

    @Test
    void 프로젝트를_삭제한다() {
        // given
        final ProjectCreationRequest projectCreationRequest = ProjectCreationRequest.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();
        final User admin = getAdmin();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequest).getProjectId();

        // when, then
        assertThatCode(() -> projectService.deleteById(projectId))
                .doesNotThrowAnyException();
    }

    @Test
    void 프로젝트에_유저를_추가한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final User admin = getAdmin();
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        assertThatCode(() -> projectService.addUsersToProject(
                projectId,
                List.of(ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build())
        )).doesNotThrowAnyException();
    }

    @Test
    void 프로젝트에_속한_유저_목록을_검색한다() {
        // given
        final User user1 = userRepository.save(User.builder().accountId("newUser1").build());
        final User user2 = userRepository.save(User.builder().accountId("newUser2").build());
        final User user3 = userRepository.save(User.builder().accountId("newUser3").build());
        final User admin = getAdmin();
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        projectService.addUsersToProject(
                projectId,
                List.of(
                        ProjectUserAdditionRequest.builder().accountId(user1.getAccountId()).role("Tester").build(),
                        ProjectUserAdditionRequest.builder().accountId(user2.getAccountId()).role("Developer").build(),
                        ProjectUserAdditionRequest.builder().accountId(user3.getAccountId()).role("ProjectLeader")
                                .build()
                ));

        // when
        final List<ProjectUserResponse> users = projectService.findUsers(projectId);

        // then
        assertThat(users).hasSize(3);
    }

    @Test
    void 프로젝트에_속한_유저를_삭제한다() {
        // given
        final User user1 = userRepository.save(User.builder().accountId("newUser1").build());
        final User user2 = userRepository.save(User.builder().accountId("newUser2").build());
        final User user3 = userRepository.save(User.builder().accountId("newUser3").build());
        final User admin = getAdmin();
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        projectService.addUsersToProject(
                projectId,
                List.of(
                        ProjectUserAdditionRequest.builder().accountId(user1.getAccountId()).role("Tester").build(),
                        ProjectUserAdditionRequest.builder().accountId(user2.getAccountId()).role("Developer").build(),
                        ProjectUserAdditionRequest.builder().accountId(user3.getAccountId()).role("ProjectLeader")
                                .build()
                ));

        // when
        projectService.deleteUsersFromProject(
                projectId,
                List.of(user1.getAccountId(), user2.getAccountId())
        );
        final List<ProjectUserResponse> users = projectService.findUsers(projectId);

        // then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getAccountId()).isEqualTo(user3.getAccountId());
    }

    @Test
    void 유저가_속한_프로젝트들을_조회한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final User admin = getAdmin();
        final Long projectId1 = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트1")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        final Long projectId2 = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트2")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        final Long projectId3 = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트3")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        projectService.addUsersToProject(
                projectId1,
                List.of(ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build())
        );
        projectService.addUsersToProject(
                projectId2,
                List.of(ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build())
        );
        projectService.addUsersToProject(
                projectId3,
                List.of(ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build())
        );

        // when
        final List<UserProjectSummaryResponse> projectSummaries = projectService.findProjectsByAccountId(
                user.getAccountId());

        // then
        assertThat(projectSummaries).hasSize(3);
    }

    @Test
    void 하나의_프로젝트와_하나의_회원간의_정보를_검색한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final User admin = getAdmin();
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        projectService.addUsersToProject(
                projectId,
                List.of(
                        ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build()
                ));

        // when
        final UserProjectAssociationResponse response = projectService.findAssociationBetweenProjectAndUser(
                user.getAccountId(), projectId);

        // then
        assertThat(response.getRole()).isEqualToIgnoringCase("Tester");
    }

    @Test
    void 프로젝트_접근시간을_갱신한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final User admin = getAdmin();
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        projectService.addUsersToProject(
                projectId,
                List.of(ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build())
        );
        projectService.renewAccessTime(user.getAccountId(), projectId);
        final String originalAccessTime = projectService.findAssociationBetweenProjectAndUser(user.getAccountId(),
                        projectId)
                .getAccessTime();

        // when
        projectService.renewAccessTime(user.getAccountId(), projectId);
        final String updatedAccessTime = projectService.findAssociationBetweenProjectAndUser(user.getAccountId(),
                        projectId)
                .getAccessTime();

        // then
        assertThat(LocalDateTime.parse(updatedAccessTime).isAfter(LocalDateTime.parse(originalAccessTime))).isTrue();
    }

    @Test
    void 프로젝트_즐겨찾기를_갱신한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final User admin = getAdmin();
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequest.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        projectService.addUsersToProject(
                projectId,
                List.of(ProjectUserAdditionRequest.builder().accountId(user.getAccountId()).role("Tester").build())
        );

        // when
        projectService.renewFavorite(user.getAccountId(), projectId, UserProjectFavoriteRequest.builder().isFavorite(true).build());
        final UserProjectSummaryResponse summary = projectService.findProjectsByAccountId(
                user.getAccountId()).get(0);

        // then
        assertThat(summary.isFavorite()).isTrue();
    }
}
