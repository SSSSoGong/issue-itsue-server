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
@PrimaryKeyJoinColumn(name = "MEMBER_ID")
public class Member extends Account {

    @OneToMany(mappedBy = "member")
    private List<Member_Project> memberProjects = new ArrayList<>();
}
