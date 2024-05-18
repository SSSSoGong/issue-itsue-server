package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "issue_id")
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixer_id")
    private User fixer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "issue")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "issue")
    @Builder.Default
    private List<IssueModification> issueModifications = new ArrayList<>();

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.getIssues().remove(this);
        }
        this.project = project;
        project.getIssues().add(this);
    }

    public void update(String title, String description, String priority) {
        if (!title.isBlank())
            this.title = title;
        if (!description.isBlank())
            this.description = description;
        if (!priority.isBlank())
            this.priority = Priority.valueOf(priority);
    }

    public void stateUpdate(String state, User assignee) {
        if (!state.isBlank())
            this.state = State.valueOf(state);
        if (assignee != null)
            this.assignee = assignee;
    }
}
