package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UPRepository extends JpaRepository<UserProject, Long> {
}
