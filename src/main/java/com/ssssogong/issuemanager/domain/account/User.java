package com.ssssogong.issuemanager.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@Table(name = "'user'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class User extends Account {

}
