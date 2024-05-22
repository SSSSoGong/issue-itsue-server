package com.ssssogong.issuemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectUpdateRequest;
import com.ssssogong.issuemanager.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@Sql(value = "/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AdminRepository adminRepository;

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
}
