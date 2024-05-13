package com.ssssogong.issuemanager.util;

import lombok.Getter;

@Getter
public class AccessUser {

    private final String accountId;
    private final boolean isAdmin;

    public AccessUser(final String accountId, final boolean isAdmin) {
        this.accountId = accountId;
        this.isAdmin = isAdmin;
    }
}
