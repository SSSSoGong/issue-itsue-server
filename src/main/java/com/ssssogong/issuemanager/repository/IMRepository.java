package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.IssueModification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMRepository extends JpaRepository<IssueModification, Long> {
}
