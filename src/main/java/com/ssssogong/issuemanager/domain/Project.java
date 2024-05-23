package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.Admin;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.util.Strings;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Issue> issues = new ArrayList<>();

    public void update(final String name, final String subject) {
        if (Strings.isNotEmpty(name)) {
            this.name = name;
        }
        if (Strings.isNotEmpty(subject)) {
            this.subject = subject;
        }
    }
}
