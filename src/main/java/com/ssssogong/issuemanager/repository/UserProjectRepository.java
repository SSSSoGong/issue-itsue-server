package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    Optional<UserProject> findByUserIdAndProjectId(Long userId, Long projectId);

    List<UserProject> findAllByProjectId(Long id);

    List<UserProject> findAllByUserId(Long id);

    @Query("SELECT up FROM UserProject up LEFT JOIN FETCH up.user LEFT JOIN FETCH up.project LEFT JOIN FETCH up.role")
    List<UserProject> findAllFetchJoinUserAndProjectAndRole();

    @Query("SELECT up FROM UserProject up JOIN FETCH up.project WHERE up.user.id = :userId")
    List<UserProject> findAllByUserIdFetchJoinProject(@Param("userId") Long userId);

    @Query("SELECT up FROM UserProject up WHERE up.user.accountId = :accountId")
    List<UserProject> findAllByAccountId(@Param("accountId") String accountId);

    @Query("SELECT up FROM UserProject up WHERE up.user.accountId = :accountId AND up.project.id = :projectId")
    Optional<UserProject> findByAccountIdAndProjectId(@Param("accountId") String accountId, @Param("projectId") long projectId);

    @Query("DELETE FROM UserProject up WHERE up.project.id = :projectId AND up.user.accountId IN :accountIds")
    @Modifying
    void deleteAllByProjectIdAndAccountIdIn(@Param("projectId") Long projectId, @Param("accountIds") List<String> accountIds);

    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN TRUE ELSE FALSE END FROM UserProject up WHERE up.project.id = :projectId AND up.user.accountId IN :accountIds")
    boolean existsByProjectIdAndAccountIdIn(@Param("projectId") Long projectId, @Param("accountIds") List<String> accountIds);

    void deleteAllByProjectId(Long id);
}

