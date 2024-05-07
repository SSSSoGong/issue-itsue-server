package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.enumeration.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member_Project {

    @Id
    @GeneratedValue
    @Column(name = "MP_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;


    public void setMember(Member member) {
        if (this.member != null)
            this.member.getMemberProjects().remove(this);
        this.member = member;
        member.getMemberProjects().add(this);
    }

    public void setProject(Project project) {
        if (this.project != null)
            this.project.getMemberProjects().remove(this);
        this.project = project;
        project.getMemberProjects().add(this);
    }
}
