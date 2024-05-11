package com.ssssogong.issuemanager.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.ssssogong.issuemanager.domain.account.Admin;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Project extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "project_id")
    private Long id;

    private String name;
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "project")
    private List<Issue> issues = new ArrayList<>();
}
