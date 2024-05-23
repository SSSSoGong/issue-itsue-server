package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
