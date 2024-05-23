package com.ssssogong.issuemanager.config;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {
    private static final String ROLE_BASE_PACKAGE = "com.ssssogong.issuemanager.domain.role";
    private final int PL_COUNT = 2;
    private final int DEV_COUNT = 10;
    private final int TESTER_COUNT = 5;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final PasswordEncoder passwordEncoder;
    private String DUMMY_ACCOUNTS_PASSWORD = "1234";

    public void saveInitialRoles() {
        final List<Role> roles = roleRepository.findAll();
        final List<Role> saves = new ArrayList<>();

        // Reflection을 사용하여 role 패키지 내의 모든 role 하위 클래스들을 찾는다
        Reflections reflections = new Reflections(ROLE_BASE_PACKAGE);
        Set<Class<? extends Role>> roleClasses = reflections.getSubTypesOf(Role.class);

        for (Class<? extends Role> roleClass : roleClasses) {
            String roleName = roleClass.getSimpleName();
            //DB에 없는 role은 반영한다
            if (isRoleNotExist(roles, roleName)) {
                try {
                    Role roleInstance = roleClass.getDeclaredConstructor().newInstance();
                    saves.add(roleInstance);
                } catch (Exception e) {
                    throw new IllegalArgumentException("DB에 Role 생성 실패: " + roleClass.getName(), e);
                }
            }
        }
        roleRepository.saveAll(saves);
    }

    private boolean isRoleNotExist(final List<Role> roles, final String expectedRoleName) {
        return roles.stream().noneMatch(each -> each.isRole(expectedRoleName));
    }

    public void saveInitialAccounts() {
        // todo 중복 체크
        System.out.println("TestAccountInitializer: saving accounts");

        User admin;
        List<User> pl = new ArrayList<>();
        List<User> dev = new ArrayList<>();
        List<User> tester = new ArrayList<>();
        Role administrator = roleRepository.findByName("Administrator").get();
        Role developer = roleRepository.findByName("Developer").get();
        Role projectLeader = roleRepository.findByName("ProjectLeader").get();
        Role testerRole = roleRepository.findByName("Tester").get();

        // admin 추가
        admin = User.builder()
                .accountId("adminID")
                .username("Hong Gildong")
                .password(passwordEncoder.encode(DUMMY_ACCOUNTS_PASSWORD))
                .build();
        // PL 추가
        for (int i = 1; i <= PL_COUNT; i++) {
            pl.add(
                    User.builder()
                            .accountId("pl" + i)
                            .username("pl" + i)
                            .password(DUMMY_ACCOUNTS_PASSWORD)
                            .build()
            );
        }
        // dev 추가
        for (int i = 1; i <= DEV_COUNT; i++) {
            dev.add(User.builder()
                    .accountId("dev" + i)
                    .username("dev" + i)
                    .password(DUMMY_ACCOUNTS_PASSWORD)
                    .build()
            );
        }
        // tester 추가
        for (int i = 1; i <= TESTER_COUNT; i++) {
            tester.add(User.builder()
                    .accountId("tester" + i)
                    .username("tester" + i)
                    .password(DUMMY_ACCOUNTS_PASSWORD)
                    .build()
            );
        }
        userRepository.save(admin);
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
        for (int i = 0; i < PL_COUNT; i++) {
            userProjects.add(UserProject.builder()
                    .project(project)
                    .user(pl.get(i))
                    .role(projectLeader)
                    .build());
        }
        for (int i = 0; i < DEV_COUNT; i++) {
            userProjects.add(UserProject.builder()
                    .project(project)
                    .user(dev.get(i))
                    .role(developer)
                    .build());
        }
        for (int i = 0; i < TESTER_COUNT; i++) {
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
