package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Administrator;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.dto.FullUserDto;
import com.ssssogong.issuemanager.dto.RegisterRequestDto;
import com.ssssogong.issuemanager.dto.UserDto;
import com.ssssogong.issuemanager.exception.ConflictException;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(value = "/truncate.sql")
@Sql(value = "/truncate_role.sql")
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
    void 회원가입을_확인한다(){
        // given
        RegisterRequestDto registerRequest = RegisterRequestDto.builder()
                .accountId("registerTest")
                .password("1234")
                .username("username")
                .build();

        // when
        UserDto savedUser = userService.save(registerRequest);

        // then
        assertThat(savedUser.getAccountId()).isEqualTo(registerRequest.getAccountId());
        assertThat(savedUser.getUsername()).isEqualTo(registerRequest.getUsername());
        assertThrows(ConflictException.class, ()->userService.save(registerRequest));
    }

    @Test
    void 로그인을_확인한다(){
        // login 메서드는 비어있음
        userService.login();
    }

    @Test
    void 유저를_검색한다() {
        // given
        final int USERS_LENGTH = 10;
        List<User> users = new ArrayList<>();
        for(int i = 0; i < USERS_LENGTH-1; i++) {
            users.add(User.builder()
                    .accountId("user" + i)
                    .username("username" + i)
                    .build());
        }
        users.add(User.builder()
                .accountId("user" + USERS_LENGTH)
                .username("username" + (USERS_LENGTH-1)) // duplicate username
                .build());
        userRepository.saveAll(users);

        // when
        List<UserDto> allUsers = (List<UserDto>) userService.findUsers();
        List<UserDto> UsersByUsername = (List<UserDto>) userService.findUsers("username" + (USERS_LENGTH-1));
        UserDto UserByAccountId = userService.findUserByAccountId("user" + USERS_LENGTH);

        // then
        assertThat(allUsers.size()).isEqualTo(USERS_LENGTH);
        assertThat(UsersByUsername.size()).isEqualTo(2);
        assertThat(UserByAccountId).isNotNull();
    }

    @Test
    void 유저_정보를_수정한다(){
        // given
        user = User.builder()
                .accountId("test")
                .username("username")
                .password("password")
                .build();
        User savedUser = userRepository.save(user);

        // when
        RegisterRequestDto updateRequest1 = RegisterRequestDto.builder()
                .accountId("")
                .username("")
                .password("")
                .build();
        FullUserDto updated1 = userService.updateUser(savedUser.getAccountId(), updateRequest1);

        RegisterRequestDto updateRequest2 = RegisterRequestDto.builder()
                .accountId("")
                .username("username 2")
                .password("password 2")
                .build();
        FullUserDto updated2 = userService.updateUser(savedUser.getAccountId(), updateRequest2);

        RegisterRequestDto updateRequest3 = RegisterRequestDto.builder()
                .accountId("should_not_be_updated")
                .username("")
                .password("")
                .build();
        FullUserDto updated3 = userService.updateUser(savedUser.getAccountId(), updateRequest3);

        // then
        assertThat(updated1.getAccountId()).isEqualTo(savedUser.getAccountId());
        assertThat(updated1.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(updated1.getPassword()).isEqualTo(savedUser.getPassword());

        assertThat(updated2.getUsername()).isEqualTo("username 2");
        assertThat(updated2.getPassword()).isEqualTo("password 2");

        assertThat(updated3.getAccountId()).isEqualTo(savedUser.getAccountId());
        assertThat(userRepository.findById(savedUser.getId()).orElseThrow().getUsername()).isEqualTo("username 2");
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