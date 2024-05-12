package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Developer extends Role{
    {
        this.allowedPrivileges = List.of( new Privilege[]{
                Privilege.ISSUE_FIXABLE,
        });
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
