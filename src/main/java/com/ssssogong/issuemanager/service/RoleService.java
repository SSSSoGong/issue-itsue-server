package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Member;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.Role;
import com.ssssogong.issuemanager.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public void saveRole(String roleName, Member member, Project project) {
        Role role = new Role();
        role.setRole(roleName);
        role.setMember(member);
        role.setProject(project);
        roleRepository.save(role);
    }

    public Role findOne(Long roleId) {
        return roleRepository.findOne(roleId);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
