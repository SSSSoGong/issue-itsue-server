package com.ssssogong.issuemanager.util;

import lombok.Getter;

@Getter
public class AccessAccount {

    private final String accountId;
    private final boolean isAdmin;
    //todo: role 추가

    public AccessAccount(final String accountId, final boolean isAdmin) {
        this.accountId = accountId;
        this.isAdmin = isAdmin;
    }
}
