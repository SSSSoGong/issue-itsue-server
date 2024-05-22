package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    Optional<UserProject> findByUserIdAndProjectId(Long userId, Long projectId);

    List<UserProject> findAllByUserId(Long id);
    @Query("SELECT up FROM UserProject up JOIN FETCH up.project WHERE up.user.id = :userId")
    List<UserProject> findAllByUserIdFetchJoinProject(@Param("userId") Long userId);
}
