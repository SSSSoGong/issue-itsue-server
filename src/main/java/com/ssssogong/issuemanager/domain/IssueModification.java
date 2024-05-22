package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class IssueModification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "im_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "'from'")
    private State from;

    @Enumerated(EnumType.STRING)
    @Column(name = "'to'")
    private State to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User modifier;

    public void setIssue(Issue issue) {
        if (this.issue != null) {
            this.issue.getIssueModifications().remove(this);
        }
        this.issue = issue;
        issue.getIssueModifications().add(this);
    }

}
