package com.ssssogong.issuemanager.domain.role;

import lombok.Getter;
import org.springframework.stereotype.Component;

public enum Privilege {
    ISSUE_REPORTABLE,
    ISSUE_ASSIGNABLE,
    ISSUE_FIXABLE,
    ISSUE_RESOLVABLE,
    ISSUE_CLOSABLE,
    ISSUE_REOPENABLE,
    ISSUE_DELETABLE,
    ISSUE_UPDATABLE,
    PROJECT_CREATABLE,
    PROJECT_DELETABLE,
    PROJECT_UPDATABLE;

    /**
     * static Bean 지정: SpEL에서 쉽게 지정하기 위한 용도<br>
     * 이걸 지정함으로써 SpEL에서 com.ssssogong.issuemanager.domain.role.Privilege 대신 @Privilege로 지정 가능
     */
    @Component("Privilege")
    @Getter
    static class SpringComponent {
        private final Privilege ISSUE_REPORTABLE = Privilege.ISSUE_REPORTABLE;
        private final Privilege ISSUE_ASSIGNABLE = Privilege.ISSUE_ASSIGNABLE;
        private final Privilege ISSUE_FIXABLE = Privilege.ISSUE_FIXABLE;
        private final Privilege ISSUE_RESOLVABLE = Privilege.ISSUE_RESOLVABLE;
        private final Privilege ISSUE_CLOSABLE = Privilege.ISSUE_CLOSABLE;
        private final Privilege ISSUE_REOPENABLE = Privilege.ISSUE_REOPENABLE;
        private final Privilege ISSUE_DELETABLE = Privilege.ISSUE_DELETABLE;
        private final Privilege ISSUE_UPDATABLE = Privilege.ISSUE_UPDATABLE;
        private final Privilege PROJECT_CREATABLE = Privilege.PROJECT_CREATABLE;
        private final Privilege PROJECT_DELETABLE = Privilege.PROJECT_DELETABLE;
        private final Privilege PROJECT_UPDATABLE = Privilege.PROJECT_UPDATABLE;
    }
}
