package com.ssssogong.issuemanager.config;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.exception.RoleSettingException;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import java.util.Random;
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
     * <p>
     * 시작 데이터 :
     * <p>
     * - Role
     * <p>
     * - 더미 계정 (admin, tester, dev, pl)
     * 각 계정 아이디는 'role{i}' 형태 -> admin, dev1, pl2, ...
     * 계정 비밀번호는 전역변수로 지정
     * <p>
     * - 더미 프로젝트
     * 프로젝트명, subject는 전역변수로 지정
     * <p>
     * - 더미 권한 (UserProject)
     * 계정명에 맞게 들어있음
     */
    private static final String ROLE_BASE_PACKAGE = "com.ssssogong.issuemanager.domain.role";
    private final String DUMMY_ACCOUNTS_PASSWORD = "1234";
    private final String DUMMY_PROJECT_NAME = "Initial Project";

    private final int PL_COUNT = 2;
    private final int DEV_COUNT = 10;
    private final int TESTER_COUNT = 5;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final PasswordEncoder passwordEncoder;
    private final IssueRepository issueRepository;


    private boolean roleDoesNotExist(final List<Role> roles, final String expectedRoleName) {
        return roles.stream().noneMatch(each -> each.isRole(expectedRoleName));
    }

    private boolean userDoesNotExist(final List<User> users, final String accountId) {
        return users.stream().noneMatch(each -> each.getAccountId().equals(accountId));
    }

    private boolean userProjectDoesNotExist(final List<UserProject> userProjects, String projectName, String accountId, String roleName) {
        return userProjects.stream().noneMatch(each ->
                (each.getProject().getName().equals(projectName)
                        && each.getUser().getAccountId().equals(accountId)
                        && each.getRole().isRole(roleName))
        );
    }

    private boolean projectDoesNotExist(final List<Project> projects, final String projectName, final String adminAccountId) {
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
            // DB에 없는 role은 반영한다
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

        try {
            // 계정 추가
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
            System.out.println("Initializer: saving users to userRepository");
            // 계정 저장
            // 이미 존재할 시, db에 저장된 엔티티로 바꿔치기 한다 (userProject 저장 시 referential error 방지)
            if (userDoesNotExist(existingUsers, admin.getAccountId())) userRepository.save(admin);
            else admin = userRepository.findByAccountId(admin.getAccountId()).get();

            for (int i = 0; i < pl.size(); i++)
                if (userDoesNotExist(existingUsers, pl.get(i).getAccountId())) userRepository.save(pl.get(i));
                else pl.set(i, userRepository.findByAccountId(pl.get(i).getAccountId()).get());

            for (int i = 0; i < dev.size(); i++)
                if (userDoesNotExist(existingUsers, dev.get(i).getAccountId())) userRepository.save(dev.get(i));
                else dev.set(i, userRepository.findByAccountId(dev.get(i).getAccountId()).get());

            for (int i = 0; i < tester.size(); i++)
                if (userDoesNotExist(existingUsers, tester.get(i).getAccountId())) userRepository.save(tester.get(i));
                else tester.set(i, userRepository.findByAccountId(tester.get(i).getAccountId()).get());

            System.out.println("Initializer: saved users");

            // 프로젝트 생성
            System.out.println("Initializer: saving projects to projectRepository");
            List<Project> existingProjects = projectRepository.findAllFetchJoinAdmin();

            Project project = Project.builder()
                    .admin(admin)
                    .name(DUMMY_PROJECT_NAME)
                    .subject("initial project created automatically")
                    .build();
            boolean projectDoesNotExist = projectDoesNotExist(existingProjects, project.getName(), admin.getAccountId());
            if (projectDoesNotExist) {
                projectRepository.save(project);
                System.out.println("Initializer: saved projects");
            } else {
                System.out.println("Initializer: project already exists");
                // 이미 존재할 시, db에 저장된 객체로 바꿔치기한다 (userProject 저장 시 referential error 방지)
                for (Project proj : existingProjects) {
                    if (project.getAdmin().getAccountId().equals(admin.getAccountId()) && proj.getName().equals(DUMMY_PROJECT_NAME)) {
                        project = proj;
                    }
                }
                System.out.println(project);
            }


            // UserProject 객체 생성
            System.out.println("Initializer: saving userProjects to userProjectRepository");
            List<UserProject> existingUserProjects = userProjectRepository.findAllFetchJoinUserAndProjectAndRole();
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
            if (userProjectDoesNotExist(existingUserProjects, project.getName(), admin.getAccountId(), administrator.getName()))
                userProjectRepository.save(userProjects.get(idx++));
            for (int i = 0; i < PL_COUNT; i++) {
                if (userProjectDoesNotExist(existingUserProjects, project.getName(), pl.get(i).getAccountId(), projectLeader.getName()))
                    userProjectRepository.save(userProjects.get(idx++));
            }
            for (int i = 0; i < TESTER_COUNT; i++) {
                if (userProjectDoesNotExist(existingUserProjects, project.getName(), tester.get(i).getAccountId(), testerRole.getName()))
                    userProjectRepository.save(userProjects.get(idx++));
            }
            for (int i = 0; i < DEV_COUNT; i++) {
                if (userProjectDoesNotExist(existingUserProjects, project.getName(), dev.get(i).getAccountId(), developer.getName()))
                    userProjectRepository.save(userProjects.get(idx++));
            }
            System.out.println("Initializer: saved userProjects");

            //이슈 저장! 통계 내려면 이슈는 다다익선이니까~ 실행될때마다 tester, category, priority 랜덤 저장됨
            Random random = new Random();
            final Category[] categories = Category.values();
            final Priority[] priorities = Priority.values();
            issueRepository.save(Issue.builder()
                    .project(project)
                    .title("prepared issue")
                    .description("this is description!")
                    .category(categories[random.nextInt(categories.length)])
                    .priority(priorities[random.nextInt(priorities.length)])
                    .state(State.NEW)
                    .reporter(tester.get(random.nextInt(tester.size())))
                    .build()
            );

        } catch (Exception e) {
//            System.out.println("Initializer Exception: " + e.getMessage());
            log.error("Initializer Exception", e);
        }

        System.out.println("TestAccountInitializer: saved initial accounts to db");
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveInitialRoles();
        saveInitialAccounts();
    }
}
