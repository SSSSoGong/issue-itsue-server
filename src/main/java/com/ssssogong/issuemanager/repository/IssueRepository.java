package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.dto.IssueProjectDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT new com.ssssogong.issuemanager.dto.IssueProjectDto(i.title, i.state, i.createdAt, i.updatedAt) " +
            "FROM Issue i " +
            "WHERE i.project.id = :projectId " +
            "AND (:title IS NULL OR i.title LIKE %:title%)" +
            "AND (:state IS NULL OR i.state = :state)")
    List<IssueProjectDto> findIssuesByProjectId(@Param("projectId") Long projectId,
                                                @Param("title") String title,
                                                @Param("state") String state);
}
