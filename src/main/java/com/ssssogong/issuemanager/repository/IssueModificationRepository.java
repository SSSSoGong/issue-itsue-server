package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueModification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueModificationRepository extends JpaRepository<IssueModification, Long> {

    List<IssueModification> findByIssueId(Long issueId);
}
