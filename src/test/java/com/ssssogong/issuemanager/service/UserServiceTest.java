package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Administrator;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.dto.UserDto;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Sql(value = "/truncate.sql")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RoleRepository roleRepository;
    private User user;
    private Project project;
    private Role role;

    private final String USER_ID = "testUser";
    private final String USERNAME = "테스트 계정";
    private final String PROJECT_NAME = "테스트 프로젝트";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .accountId(USER_ID)
                .username(USERNAME)
                .password("password")
                .build();
        project = Project.builder()
                .name(PROJECT_NAME)
                .subject("테스트 프로젝트입니다")
                .build();
        role = new Administrator();
        userRepository.save(user);
        projectRepository.save(project);
        roleRepository.save(role);
    }

    @Test
    void login_동작을_확인한다() {
        // given

        // when

        // then

    }

    @Test
    void unregister_동작을_확인한다() {
        // given

        // when

        // then

    }

    @Test
    void 유저를_검색한다() {
        // given

        // when

        // then

    }

    @Test
    void 프로젝트에서_Role을_파악한다() {
        // given
        Project savedProject = projectRepository.findAll().get(0);
        User savedUser = userRepository.findByAccountId(USER_ID).orElseThrow();
        Role savedRole = roleRepository.findByName(role.getRoleName()).orElseThrow();
        UserProject userProject = UserProject.builder()
                .user(savedUser)
                .project(savedProject)
                .role(savedRole)
                .build();
        userProjectRepository.save(userProject);
        // when
        String roleName = userService.findProjectRoleName(savedUser.getAccountId(), savedUser.getId());

        // then
        assertThat(roleName).isEqualTo(role.getRoleName());
    }

    @Test
    void 계정ID로_검색한다() {
        // given
        String dummyId = "random id";
        String dummyId2 = "";

        // when
        UserDto user1 = userService.findUserByAccountId(USER_ID);

        // then
        assertThat(user1.getAccountId()).isEqualTo(USER_ID);
        Assertions.assertThrows(NotFoundException.class, ()->userService.findUserByAccountId(dummyId));
        Assertions.assertThrows(NotFoundException.class, ()->userService.findUserByAccountId(dummyId2));
    }

    @Test
    void 유저의_권한을_조회한다() {
        // given
        Role savedRole = roleRepository.findByName(role.getRoleName()).orElseThrow();
        User savedUser = userRepository.findByAccountId(USER_ID).orElseThrow();
        userProjectRepository.save(
                UserProject.builder()
                        .user(user)
                        .project(project)
                        .role(savedRole)
                        .build()
        );

        // when
        Collection<GrantedAuthority> auths = userService.getAuthorities(savedUser.getId());
        System.out.println(role.getRoleName());
        System.out.println("auths : ");
        auths.stream().forEach(each -> System.out.println(each.getAuthority()));
        int totalPrivileges = auths.size();
        int RolePrivilege = (int) auths.stream().filter(each ->
                    each.getAuthority().toUpperCase().contains(role.getRoleName().toUpperCase())

        ).count();

        // then
        assertThat(totalPrivileges).isEqualTo(role.getAllowedPrivileges().size() + 1);
        assertThat(RolePrivilege).isEqualTo(1);
    }
}