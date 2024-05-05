package com.ssssogong.issuemanager.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    @JoinColumn(name = "ACCOUNT_ID")
    private Admin admin;

    @OneToMany(mappedBy = "project")
    private List<Role> roles = new ArrayList<>();

}
