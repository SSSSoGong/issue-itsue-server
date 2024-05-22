package com.ssssogong.issuemanager.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//@Sql(value = "/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void Admin이_프로젝트를_생성한다() {
        //given
        final Admin admin = Admin.builder()
                .accountId("admin")
                .username("어드민")
                .password("password").build();
        adminRepository.save(admin);
        final ProjectCreationRequest projectCreationRequest = ProjectCreationRequest.builder()
                .name("야심찬 프로젝트")
                .subject("어쩌구 저쩌구 프로젝트입니다.")
                .build();

        //when, then
        assertThatCode(() -> projectService.create(admin.getAccountId(), projectCreationRequest))
                .doesNotThrowAnyException();
    }
}
