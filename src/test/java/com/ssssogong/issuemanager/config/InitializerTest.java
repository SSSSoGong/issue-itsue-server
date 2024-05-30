package com.ssssogong.issuemanager.config;

import com.ssssogong.issuemanager.domain.Roles;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.*;
import com.ssssogong.issuemanager.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "/truncate.sql")
public class InitializerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserProjectRepository userProjectRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    Initializer initializer;

    @Test
    void role_생성을_확인한다(){
        // given
        // Initializer는 내부적으로 Reflection.getSubTypesOf(Class)를 사용하여 역할을 불러온다.
        // 다만 해당 로직이 잘못될 것을 감안하여 테스트에서는 역할을 손으로 적는다
        // (역할 추가 및 수정이 이뤄지는 경우 손수 수정해야하는 것은 감수)
        List<String> roleNames = List.of("Administrator", "Developer", "ProjectLeader", "Tester");

        // when
        initializer.saveInitialRoles();
        List<Role> savedRoles = roleRepository.findAll();

        // then
        assertThat(savedRoles.size()).isEqualTo(roleNames.size());
        for(String roleName : roleNames) {
            int matchedRoleCount = savedRoles.stream()
                    .filter(each -> each.isRole(roleName))
                    .toList().size();
            assertThat(matchedRoleCount).isEqualTo(1);
        }
    }

    @Test
    void 더미_계정_생성을_확인한다(){
        // given
        int adminCount = initializer.ADMIN_COUNT;
        int devCount = initializer.DEV_COUNT;
        int plCount = initializer.PL_COUNT;
        int testerCount = initializer.TESTER_COUNT;
        int totalUserCount = adminCount + devCount + plCount + testerCount;

        // when
        List<User> usersBefore = userRepository.findAll();
        initializer.saveInitialAccounts();
        List<User> savedUsers = userRepository.findAll();
        int savedDevCount = (int) savedUsers.stream().filter(each -> {
                return each.getAccountId().contains("dev");
            }).count();
        int savedAdminCount = (int) savedUsers.stream().filter(each -> {
                return each.getAccountId().contains("admin");
            }).count();
        int savedPlCount = (int) savedUsers.stream().filter(each -> {
                return each.getAccountId().contains("pl");
            }).count();
        int savedTesterCount = (int) savedUsers.stream().filter(each -> {
                return each.getAccountId().contains("tester");
            }).count();

        // then
        assertThat(usersBefore.size()).isEqualTo(0);
        assertThat(savedUsers.size()).isEqualTo(totalUserCount);
        assertThat(savedAdminCount).isEqualTo(adminCount);
        assertThat(savedDevCount).isEqualTo(devCount);
        assertThat(savedPlCount).isEqualTo(plCount);
        assertThat(savedTesterCount).isEqualTo(testerCount);
    }

    @Transactional
    @Test
    void 권한_저장을_확인한다(){
        // given
        int adminCount = initializer.ADMIN_COUNT;
        int devCount = initializer.DEV_COUNT;
        int plCount = initializer.PL_COUNT;
        int testerCount = initializer.TESTER_COUNT;
        int totalUserCount = adminCount + devCount + plCount + testerCount;

        // when
        List<UserProject> userProjectsBefore = userProjectRepository.findAll();
        initializer.saveInitialAccounts();
        List<UserProject> savedUserProjects = userProjectRepository.findAll();
        int savedAdminCount = (int) savedUserProjects.stream().filter(each ->
                each.getUser().getAccountId().contains("admin")
        ).count();
        int savedDevCount = (int) savedUserProjects.stream().filter(each ->
                each.getUser().getAccountId().contains("dev")
            ).count();
        int savedPlCount = (int) savedUserProjects.stream().filter(each ->
                each.getUser().getAccountId().contains("pl")
        ).count();
        int savedTesterCount = (int) savedUserProjects.stream().filter(each ->
                each.getUser().getAccountId().contains("tester")
        ).count();

        // then
        assertThat(savedUserProjects.size()).isEqualTo(totalUserCount);
        assertThat(savedAdminCount).isEqualTo(adminCount);
        assertThat(savedDevCount).isEqualTo(devCount);
        assertThat(savedPlCount).isEqualTo(plCount);
        assertThat(savedTesterCount).isEqualTo(testerCount);
    }
}
