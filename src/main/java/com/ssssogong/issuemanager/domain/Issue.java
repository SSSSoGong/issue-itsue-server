package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.enumeration.Issue_State;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Issue {

    @Id
    @GeneratedValue
    @Column(name = "ISSUE_ID")
    private Long id;

    private String title;
    private String description;
    private String reporter;
    private LocalDateTime reported_date;
    private String fixer;
    private String assignee;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Issue_State state;

    @OneToMany
    @JoinColumn(name = "COMMENT_ID")
    private List<Comment> comments = new ArrayList<>();
}
