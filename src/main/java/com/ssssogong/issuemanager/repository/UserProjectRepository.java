package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.UserProject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    List<UserProject> findAllByProjectId(Long id);
}

