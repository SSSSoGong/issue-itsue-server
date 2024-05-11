package com.ssssogong.issuemanager.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "admin_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Admin extends Account {


}