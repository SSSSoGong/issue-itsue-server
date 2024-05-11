package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
