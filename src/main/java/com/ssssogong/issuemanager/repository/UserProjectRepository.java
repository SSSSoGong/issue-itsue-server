package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.UserProject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    List<UserProject> findAllByProjectId(Long id);

    @Query("SELECT up FROM UserProject up WHERE up.user.accountId = :accountId")
    List<UserProject> findAllByAccountId(@Param("accountId") String accountId);

    @Query("SELECT up FROM UserProject up WHERE up.user.accountId = :accountId and up.project.id = :projectId")
    Optional<UserProject> findByAccountIdAndProjectId(@Param("accountId") String accountId, @Param("projectId") String projectId);
}

