package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.Entity;
import java.util.List;

@Entity
public class Tester extends Role{
    {
        this.allowedPrivileges = List.of( new Privilege[]{
                Privilege.ISSUE_REPORTABLE,
                Privilege.ISSUE_RESOLVABLE,
                Privilege.ISSUE_REOPENABLE,
        });
    }
}
