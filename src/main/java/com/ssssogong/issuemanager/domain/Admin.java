package com.ssssogong.issuemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("A")
@PrimaryKeyJoinColumn(name = "ADMIN_ID")
public class Admin extends Account {

    @OneToMany(mappedBy = "admin")
    private List<Project> projects = new ArrayList<>();
}
