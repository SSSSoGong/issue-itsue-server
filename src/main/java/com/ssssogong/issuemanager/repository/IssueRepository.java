package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Issue;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    @Query("SELECT i FROM Issue i WHERE i.createdAt >= :startDate AND i.project.id = :projectId")
    List<Issue> findIssuesCreatedSinceAndByProjectId(@Param("projectId") Long projectId, @Param("startDate") LocalDateTime startDate);
}
