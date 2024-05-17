package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.role.Developer;
import com.ssssogong.issuemanager.domain.role.ProjectLeader;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.domain.role.Tester;
import com.ssssogong.issuemanager.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void updateRoles() {
        final List<Role> roles = roleRepository.findAll();
        final List<Role> saves = new ArrayList<>();
        //todo: 새로운 Role이 추가되어도 알잘딱하게 추가해주기
        if(isRoleNotExist(roles, "Tester")) {
            saves.add(new Tester());
        }
        if(isRoleNotExist(roles, "Developer")) {
            saves.add(new Developer());
        }
        if(isRoleNotExist(roles, "ProjectLeader")) {
            saves.add(new ProjectLeader());
        }
        roleRepository.saveAll(saves);
    }

    private boolean isRoleNotExist(final List<Role> roles, final String expectedRoleName) {
        return roles.stream().noneMatch(each -> each.isRole(expectedRoleName));
    }
}
