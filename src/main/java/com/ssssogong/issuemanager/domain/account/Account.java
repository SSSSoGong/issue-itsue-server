package com.ssssogong.issuemanager.domain.account;

import com.ssssogong.issuemanager.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class Account extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "account_id", unique = true)
    private String accountId;

    private String password;
    private String username;
}