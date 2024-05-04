package com.ssssogong.issuemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("U")
public class User {

    @OneToMany(mappedBy = "user")
    private List<Role> roles = new ArrayList<>();
}
