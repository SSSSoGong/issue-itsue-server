package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.Entity;
import java.util.List;

@Entity
public class Developer extends Role{
    {
        this.allowedPrivileges = List.of(
                Privilege.ISSUE_FIXABLE
        );
    }

}
