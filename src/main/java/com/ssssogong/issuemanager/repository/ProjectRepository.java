package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.admin")
    List<Project> findAllFetchJoinAdmin();
}
