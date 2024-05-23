package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class Administrator extends Role {
    {
        this.allowedPrivileges = List.of(
                // TODO : Admin은 모든 권한을 줘도 괜찮지 않을까?
                Privilege.PROJECT_CREATABLE,
                Privilege.PROJECT_UPDATABLE,
                Privilege.PROJECT_DELETABLE,
                Privilege.ISSUE_CLOSABLE,
                Privilege.ISSUE_ASSIGNABLE,
                Privilege.ISSUE_FIXABLE,
                Privilege.ISSUE_REOPENABLE,
                Privilege.ISSUE_REPORTABLE,
                Privilege.ISSUE_RESOLVABLE
        );
    }
}
