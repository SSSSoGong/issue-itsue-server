package com.ssssogong.issuemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "PROJECT_ID")
    private Long id;

    private String projectName;
    private String subject;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID")
    private Admin admin;

    @OneToMany(mappedBy = "project")
    private List<Member_Project> memberProjects = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "PROJECT_ID")
    private List<Issue> issues = new ArrayList<>();


    public void setAdmin(Admin admin) {
        if (this.admin != null)
            this.admin.getProjects().remove(this);
        this.admin = admin;
        admin.getProjects().add(this);
    }
}
