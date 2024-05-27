package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
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

    private User admin;

    @BeforeEach
    void setUp() {
        this.admin = User.builder()
                .accountId("adminAccount")
                .username("어드민")
                .password("password").build();
        userRepository.save(admin);
    }

    @Test
    void Admin이_프로젝트를_생성한다() {
        //given
        final ProjectCreationRequestDto projectCreationRequestDto = ProjectCreationRequestDto.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();

        //when, then
        assertThatCode(() -> projectService.create(admin.getAccountId(), projectCreationRequestDto))
                .doesNotThrowAnyException();
    }

    @Test
    void 프로젝트_정보를_조회한다() {
        // given
        final ProjectCreationRequestDto projectCreationRequestDto = ProjectCreationRequestDto.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequestDto).getProjectId();

        // when
        final ProjectDetailsResponseDto response = projectService.findById(projectId);

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
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 프로젝트_정보를_수정한다() {
        // given
        final ProjectCreationRequestDto projectCreationRequestDto = ProjectCreationRequestDto.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequestDto).getProjectId();
        final ProjectUpdateRequestDto projectUpdateRequestDto = ProjectUpdateRequestDto.builder()
                .name("야심찬 프로젝트(수정)")
                .subject("어쩌구 저쩌구 프로젝트입니다.(수정)")
                .build();

        // when
        projectService.updateById(projectId, projectUpdateRequestDto);
        final ProjectDetailsResponseDto response = projectService.findById(projectId);

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
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_DELETABLE_TO_PROJECT_1"})
    void 프로젝트를_삭제한다() {
        // given
        final ProjectCreationRequestDto projectCreationRequestDto = ProjectCreationRequestDto.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequestDto).getProjectId();

        // when, then
        assertThatCode(() -> projectService.deleteById(projectId))
                .doesNotThrowAnyException();
    }

    @Test
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 프로젝트에_유저를_추가한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        assertThatCode(() -> projectService.addUsersToProject(
                projectId,
                List.of(ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build())
        )).doesNotThrowAnyException();
    }

    @Test
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 프로젝트에_속한_유저_목록을_검색한다() {
        // given
        final User user1 = userRepository.save(User.builder().accountId("newUser1").build());
        final User user2 = userRepository.save(User.builder().accountId("newUser2").build());
        final User user3 = userRepository.save(User.builder().accountId("newUser3").build());
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        projectService.addUsersToProject(
                projectId,
                List.of(
                        ProjectUserAdditionRequestDto.builder().accountId(user1.getAccountId()).role("Tester").build(),
                        ProjectUserAdditionRequestDto.builder().accountId(user2.getAccountId()).role("Developer").build(),
                        ProjectUserAdditionRequestDto.builder().accountId(user3.getAccountId()).role("ProjectLeader")
                                .build()
                ));

        // when
        final List<ProjectUserResponseDto> users = projectService.findUsers(projectId);

        // then
        assertThat(users).hasSize(3);
    }

    @Test
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 프로젝트에_속한_유저를_삭제한다() {
        // given
        final User user1 = userRepository.save(User.builder().accountId("newUser1").build());
        final User user2 = userRepository.save(User.builder().accountId("newUser2").build());
        final User user3 = userRepository.save(User.builder().accountId("newUser3").build());
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        projectService.addUsersToProject(
                projectId,
                List.of(
                        ProjectUserAdditionRequestDto.builder().accountId(user1.getAccountId()).role("Tester").build(),
                        ProjectUserAdditionRequestDto.builder().accountId(user2.getAccountId()).role("Developer").build(),
                        ProjectUserAdditionRequestDto.builder().accountId(user3.getAccountId()).role("ProjectLeader")
                                .build()
                ));

        // when
        projectService.deleteUsersFromProject(
                projectId,
                List.of(user1.getAccountId(), user2.getAccountId())
        );
        final List<ProjectUserResponseDto> users = projectService.findUsers(projectId);

        // then
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getAccountId()).isEqualTo(user3.getAccountId());
    }

    @Test
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1", "PROJECT_UPDATABLE_TO_PROJECT_2", "PROJECT_UPDATABLE_TO_PROJECT_3"})
    void 유저가_속한_프로젝트들을_조회한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final Long projectId1 = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트1")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        final Long projectId2 = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트2")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        final Long projectId3 = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트3")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        System.out.println("projectId3 = " + projectId3);
        System.out.println("projectId1 = " + projectId1);
        System.out.println("projectId2 = " + projectId2);
        projectService.addUsersToProject(
                projectId1,
                List.of(ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build())
        );
        projectService.addUsersToProject(
                projectId2,
                List.of(ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build())
        );
        projectService.addUsersToProject(
                projectId3,
                List.of(ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build())
        );

        // when
        final List<UserProjectSummaryResponseDto> projectSummaries = projectService.findProjectsByAccountId(
                user.getAccountId());

        // then
        assertThat(projectSummaries).hasSize(3);
    }

    @Test
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 하나의_프로젝트와_하나의_회원간의_정보를_검색한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();
        projectService.addUsersToProject(
                projectId,
                List.of(
                        ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build()
                ));

        // when
        final UserProjectAssociationResponseDto response = projectService.findAssociationBetweenProjectAndUser(
                user.getAccountId(), projectId);

        // then
        assertThat(response.getRole()).isEqualToIgnoringCase("Tester");
    }

    @Test
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 프로젝트_접근시간을_갱신한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        projectService.addUsersToProject(
                projectId,
                List.of(ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build())
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
    @WithMockUser(username = "adminAccount", authorities = {"PROJECT_UPDATABLE_TO_PROJECT_1"})
    void 프로젝트_즐겨찾기를_갱신한다() {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final Long projectId = projectService.create(
                admin.getAccountId(),
                ProjectCreationRequestDto.builder()
                        .name("야심찬 프로젝트")
                        .subject("어쩌구 저쩌구 프로젝트입니다.")
                        .build()
        ).getProjectId();

        projectService.addUsersToProject(
                projectId,
                List.of(ProjectUserAdditionRequestDto.builder().accountId(user.getAccountId()).role("Tester").build())
        );

        // when
        projectService.renewFavorite(user.getAccountId(), projectId, UserProjectFavoriteRequestDto.builder().isFavorite(true).build());
        final UserProjectSummaryResponseDto summary = projectService.findProjectsByAccountId(
                user.getAccountId()).get(0);

        // then
        assertThat(summary.isFavorite()).isTrue();
    }
}
