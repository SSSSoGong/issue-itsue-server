package com.ssssogong.issuemanager.domain.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

}
