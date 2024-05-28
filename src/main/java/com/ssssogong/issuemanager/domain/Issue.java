package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<IssueModification> issueModifications = new ArrayList<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<IssueImage> issueImages = new ArrayList<>();

    public void setProject(Project project) {
        if (this.project != null) {
            this.project.getIssues().remove(this);
        }
        this.project = project;
        project.getIssues().add(this);
    }

    public void update(String title, String description, String priority) {
        if (title != null && !title.isBlank())
            this.title = title;
        if (description != null && !description.isBlank())
            this.description = description;
        if (priority != null && !priority.isBlank())
            this.priority = Priority.valueOf(priority);
    }

    public void update(String state) {
        if(state != null && !state.isBlank())
            this.state = State.valueOf(state);

    }

    public boolean hasResolvedHistory() {
        return issueModifications.stream().anyMatch(each -> each.getTo() == State.RESOLVED);
    }
}
