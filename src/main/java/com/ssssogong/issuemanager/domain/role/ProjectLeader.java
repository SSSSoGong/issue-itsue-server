package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.Entity;
import java.util.List;

@Entity
public class ProjectLeader extends Role {
    {
        this.allowedPrivileges = List.of(
                Privilege.ISSUE_ASSIGNABLE,
                Privilege.ISSUE_CLOSABLE,
                Privilege.ISSUE_DELETABLE
        );
    }
}
