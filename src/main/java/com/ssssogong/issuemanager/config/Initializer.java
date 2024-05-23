package com.ssssogong.issuemanager.config;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.Account;
import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.*;
import com.ssssogong.issuemanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.swing.text.html.StyleSheet;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {
    private final int PL_COUNT = 2;
    private final int DEV_COUNT = 10;
    private final int TESTER_COUNT = 5;
    private String DUMMY_ACCOUNTS_PASSWORD = "1234";

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveInitialRoles(){
        System.out.println("TestAccountInitializer: saving roles");
        final List<Role> roles = roleRepository.findAll();
        final List<Role> saves = List.of(new Administrator(), new Tester(), new Developer(), new ProjectLeader());
        //todo: 새로운 Role이 추가되어도 알잘딱하게 추가해주기
        roleRepository.saveAll(saves);
        System.out.println("TestAccountInitializer: saved roles");
    }

    public void saveInitialAccounts(){
        System.out.println("TestAccountInitializer: saving accounts");


        Admin admin;
        List<User> pl = new ArrayList<>();
        List<User> dev = new ArrayList<>();
        List<User> tester = new ArrayList<>();
        Role administrator = roleRepository.findByName("Administrator").get();
        Role developer = roleRepository.findByName("Developer").get();
        Role projectLeader = roleRepository.findByName("ProjectLeader").get();
        Role testerRole = roleRepository.findByName("Tester").get();

        // admin 추가
        admin = Admin.builder()
                .accountId("adminID")
                .username("Hong Gildong")
                .password(passwordEncoder.encode(DUMMY_ACCOUNTS_PASSWORD))
                .build();
        // PL 추가
        for(int i = 1; i <= PL_COUNT; i++){
            pl.add(
                    User.builder()
                            .accountId("pl" + i)
                            .username("pl" + i)
                            .password(DUMMY_ACCOUNTS_PASSWORD)
                            .build()
            );
        }
        // dev 추가
        for(int i = 1; i <= DEV_COUNT; i++){
            dev.add(User.builder()
                    .accountId("dev" + i)
                    .username("dev" + i)
                    .password(DUMMY_ACCOUNTS_PASSWORD)
                    .build()
            );
        }
        // tester 추가
        for(int i = 1; i <= TESTER_COUNT; i++){
            tester.add(User.builder()
                    .accountId("tester" + i)
                    .username("tester" + i)
                    .password(DUMMY_ACCOUNTS_PASSWORD)
                    .build()
            );
        }
        adminRepository.save((Admin)admin);
        userRepository.saveAll(pl);
        userRepository.saveAll(dev);
        userRepository.saveAll(tester);
        // 프로젝트 생성
        Project project = Project.builder()
                .admin(admin)
                .name("initial project")
                .subject("initial project created automatically")
                .build();
        projectRepository.save(project);
        // UserProject 객체 생성
        List<UserProject> userProjects = new ArrayList<>();
//        userProjects.add(UserProject.builder()
//                        .project(project)
//                        .user(admin)
//                        .role(administrator)
//                        .build());
        for(int i = 0; i < PL_COUNT; i++){
            userProjects.add(UserProject.builder()
                            .project(project)
                            .user(pl.get(i))
                            .role(projectLeader)
                            .build());
        }
        for(int i = 0; i < DEV_COUNT; i++){
            userProjects.add(UserProject.builder()
                            .project(project)
                            .user(dev.get(i))
                            .role(developer)
                            .build());
        }
        for(int i = 0; i < TESTER_COUNT; i++){
            userProjects.add(UserProject.builder()
                            .project(project)
                            .user(tester.get(i))
                            .role(testerRole)
                            .build());
        }
        userProjectRepository.saveAll(userProjects);


        System.out.println("TestAccountInitializer: saved initial accounts to db");
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveInitialRoles();
        saveInitialAccounts();
    }
}
