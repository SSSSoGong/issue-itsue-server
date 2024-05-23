package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.IssueImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IssueImageRepository extends JpaRepository<IssueImage, Long> {

    Optional<IssueImage> deleteByIssueId(Long issueId);

    List<IssueImage> findByIssueId(Long issueId);
}
