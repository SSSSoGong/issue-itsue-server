package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Tester extends Role{

    {
        this.allowedPrivileges = List.of( new Privilege[]{
                Privilege.ISSUE_REPORTABLE,
                Privilege.ISSUE_RESOLVABLE,
                Privilege.ISSUE_REOPENABLE,
        });
    }

    @Transient
    @Override
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        // 자신의 역할 + Privilege를 담아서 반환한다
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.getClass().getName()));
        authorities.addAll(
                allowedPrivileges
                        .stream()
                        .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                        .toList()
        );
        return authorities;
    }
}
