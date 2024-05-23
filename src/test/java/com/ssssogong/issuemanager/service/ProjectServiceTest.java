package com.ssssogong.issuemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectUpdateRequest;
import com.ssssogong.issuemanager.dto.ProjectUserAdditionRequest;
import com.ssssogong.issuemanager.repository.AdminRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProjectRepository userProjectRepository;

    private Admin getAdmin() {
        final Admin admin = Admin.builder()
                .accountId("admin")
                .username("어드민")
                .password("password").build();
        adminRepository.save(admin);
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
        final Admin admin = getAdmin();
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
        final Admin admin = getAdmin();
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
        final Admin admin = getAdmin();
        final Long projectId = projectService.create(admin.getAccountId(), projectCreationRequest).getProjectId();

        // when, then
        assertThatCode(() -> projectService.deleteById(projectId))
                .doesNotThrowAnyException();
    }

    //todo: 프로젝트에 유저 추가
    //todo: 프로젝트에 속한 유저 목록 검색
    //todo: 프로젝트에서 유저 삭제
    //todo: 특정 회원이 속한 프로젝트 목록 검색
    //todo: 프로젝트-회원 간 정보 검색
    @Test
    void 프로젝트_접근시간을_갱신한다(@Autowired RoleRepository roleRepository) {
        // given
        final User user = userRepository.save(User.builder().accountId("newUser").build());
        final Admin admin = getAdmin();
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
}
