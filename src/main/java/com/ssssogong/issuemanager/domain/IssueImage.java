package com.ssssogong.issuemanager.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssueImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @Builder
    public IssueImage(Long id, String imageUrl, Issue issue) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.issue = issue;
    }

    public void setIssue(Issue issue) {
        if (this.issue != null) {
            this.issue.getIssueImages().remove(this);
        }
        this.issue = issue;
        issue.getIssueImages().add(this);
    }
}
