package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.dto.IssueProjectResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);
}
