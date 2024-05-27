package com.ssssogong.issuemanager.config;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.exception.RoleSettingException;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {
    /**
     * 스프링 실행 시 시작 데이터 넣어주는 클래스
     *
     * 시작 데이터 :
     *
     * - Role
     *
     * - 더미 계정 (admin, tester, dev, pl)
     *   각 계정 아이디는 'role{i}' 형태 -> admin, dev1, pl2, ...
     *   계정 비밀번호는 전역변수로 지정
     *
     * - 더미 프로젝트
     *   프로젝트명, subject는 전역변수로 지정
     *
     * - 더미 권한 (UserProject)
     *   계정명에 맞게 들어있음
     */
    private static final String ROLE_BASE_PACKAGE = "com.ssssogong.issuemanager.domain.role";
    private final int PL_COUNT = 2;
    private final int DEV_COUNT = 10;
    private final int TESTER_COUNT = 5;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final PasswordEncoder passwordEncoder;

    private final String DUMMY_ACCOUNTS_PASSWORD = "1234";

    private boolean roleDoesNotExist(final List<Role> roles, final String expectedRoleName) {
        return roles.stream().noneMatch(each -> each.isRole(expectedRoleName));
    }

    private boolean userDoesNotExist(final List<User> users, final String accountId){
        return users.stream().noneMatch(each -> each.getAccountId().equals(accountId));
    }

    private boolean userProjectDoesNotExist(final List<UserProject> userProjects, String projectName, String accountId, String roleName){
        return userProjects.stream().noneMatch(each ->
                (each.getProject().getName().equals(projectName)
                        && each.getUser().getAccountId().equals(accountId)
                        && each.getRole().isRole(roleName))
        );
    }

    private boolean projectDoesNotExist(final List<Project> projects, final String projectName, final String adminAccountId){
        return projects.stream().noneMatch(each ->
                (each.getName().equals(projectName) && each.getAdmin().getAccountId().equals(adminAccountId))
        );
    }

    public void saveInitialRoles() {
        final List<Role> roles = roleRepository.findAll();
        final List<Role> saves = new ArrayList<>();

        // Reflection을 사용하여 role 패키지 내의 모든 role 하위 클래스들을 찾는다
        Reflections reflections = new Reflections(ROLE_BASE_PACKAGE);
        Set<Class<? extends Role>> roleClasses = reflections.getSubTypesOf(Role.class);

        for (Class<? extends Role> roleClass : roleClasses) {
            String roleName = roleClass.getSimpleName();
            //DB에 없는 role은 반영한다
            if (roleDoesNotExist(roles, roleName)) {
                try {
                    Role roleInstance = roleClass.getDeclaredConstructor().newInstance();
                    saves.add(roleInstance);
                } catch (Exception e) {
                    log.error("DB에 Role 생성 실패: " + roleClass.getName(), e);
                    throw new RoleSettingException("DB에 Role 생성 실패: " + roleClass.getName());
                }
            }
        }
        roleRepository.saveAll(saves);
    }



    public void saveInitialAccounts() {
        // todo 중복 체크
        System.out.println("TestAccountInitializer: saving accounts");

        User admin;
        List<User> existingUsers = userRepository.findAll();
        List<User> pl = new ArrayList<>();
        List<User> dev = new ArrayList<>();
        List<User> tester = new ArrayList<>();

        Role administrator = roleRepository.findByName("Administrator").get();
        Role developer = roleRepository.findByName("Developer").get();
        Role projectLeader = roleRepository.findByName("ProjectLeader").get();
        Role testerRole = roleRepository.findByName("Tester").get();

        try {        // admin 추가
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
            if (userDoesNotExist(existingUsers, admin.getAccountId())) userRepository.save(admin);
            for (User p : pl)
                if (userDoesNotExist(existingUsers, p.getAccountId())) userRepository.save(p);
            for (User d : dev)
                if (userDoesNotExist(existingUsers, d.getAccountId())) userRepository.save(d);
            for (User t : tester)
                if (userDoesNotExist(existingUsers, admin.getAccountId())) userRepository.save(t);

            // 프로젝트 생성
            List<Project> existingProjects = projectRepository.findAll();
            Project project = Project.builder()
                    .admin(admin)
                    .name("initial project")
                    .subject("initial project created automatically")
                    .build();
            if (projectDoesNotExist(existingProjects, project.getName(), admin.getAccountId()))
                projectRepository.save(project);
            // UserProject 객체 생성
            List<UserProject> existingUserProjects = userProjectRepository.findAll();
            List<UserProject> userProjects = new ArrayList<>();

            userProjects.add(UserProject.builder()
                    .project(project)
                    .user(admin)
                    .role(administrator)
                    .build());
            for (int i = 0; i < PL_COUNT; i++) {
                userProjects.add(UserProject.builder()
                        .project(project)
                        .user(pl.get(i))
                        .role(projectLeader)
                        .build());
            }
            for (int i = 0; i < TESTER_COUNT; i++) {
                userProjects.add(UserProject.builder()
                        .project(project)
                        .user(tester.get(i))
                        .role(testerRole)
                        .build());
            }
            for (int i = 0; i < DEV_COUNT; i++) {
                userProjects.add(UserProject.builder()
                        .project(project)
                        .user(dev.get(i))
                        .role(developer)
                        .build());
            }

            int idx = 0;
            if (userProjectDoesNotExist(userProjects, project.getName(), admin.getAccountId(), admin.toString()))
                userProjectRepository.save(userProjects.get(idx++));
            for (int i = 0; i < PL_COUNT; i++) {
                if (userProjectDoesNotExist(userProjects, project.getName(), pl.get(i).getAccountId(), pl.get(i).toString()))
                    userProjectRepository.save(userProjects.get(idx++));
            }
            for (int i = 0; i < TESTER_COUNT; i++) {
                if (userProjectDoesNotExist(userProjects, project.getName(), tester.get(i).getAccountId(), tester.get(i).toString()))
                    userProjectRepository.save(userProjects.get(idx++));
            }
            for (int i = 0; i < DEV_COUNT; i++) {
                if (userProjectDoesNotExist(userProjects, project.getName(), dev.get(i).getAccountId(), dev.get(i).toString()))
                    userProjectRepository.save(userProjects.get(idx++));
            }
        }
        catch (Exception e){
            System.out.println("Initializer Exception: " + e.getMessage());
        }

        System.out.println("TestAccountInitializer: saved initial accounts to db");
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveInitialRoles();
        saveInitialAccounts();
    }
}
