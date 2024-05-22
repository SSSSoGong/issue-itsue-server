package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.role.Developer;
import com.ssssogong.issuemanager.domain.role.ProjectLeader;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.domain.role.Tester;
import com.ssssogong.issuemanager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleInitializer {

    private static final String ROLE_BASE_PACKAGE = "com.ssssogong.issuemanager.domain.role";
    private final RoleRepository roleRepository;

    @PostConstruct
    public void updateRoles() {
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
}
