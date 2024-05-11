package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProject {

    @Id
    @GeneratedValue
    @Column(name = "user_project_id")
    private Long id;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    @Column(name = "access_time")
    private LocalDateTime accessTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Builder
    public UserProject(Boolean isFavorite, LocalDateTime accessTime, User user, Project project, Role role) {
        this.isFavorite = isFavorite;
        this.accessTime = accessTime;
        this.user = user;
        this.project = project;
        this.role = role;
    }
}
