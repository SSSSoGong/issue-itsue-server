package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.role.Role;
import lombok.Builder;

import java.util.List;

@Builder
public class Roles {

    private List<Role> roles;

    public Role findRole(final String roleName) {
        return roles.stream()
                .filter(each -> each.isRole(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역할을 찾지 못함"));
    }
}
