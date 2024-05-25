package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 프로젝트 생성
     */
    @PostMapping("/projects")
    public ResponseEntity<ProjectIdResponseDto> create(@RequestBody ProjectCreationRequestDto projectCreationRequestDto) {
        final String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        final ProjectIdResponseDto response = projectService.create(accountId, projectCreationRequestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트 정보 확인
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDetailsResponseDto> findById(@PathVariable("id") Long id) {
        final ProjectDetailsResponseDto response = projectService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트 정보 수정
     */
    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectIdResponseDto> updateById(@PathVariable("id") Long id,
                                                           @RequestBody ProjectUpdateRequestDto projectUpdateRequestDto) {
        final ProjectIdResponseDto response = projectService.updateById(id, projectUpdateRequestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트 삭제
     */
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectIdResponseDto> deleteById(@PathVariable("id") Long id) {
        final ProjectIdResponseDto response = projectService.deleteById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트에 유저(들) 추가
     */
    @PostMapping("/projects/{id}/users")
    public ResponseEntity<Void> addUsers(@PathVariable("id") Long id,
                                         @RequestBody List<ProjectUserAdditionRequestDto> request) {
        projectService.addUsersToProject(id, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 프로젝트에 속한 유저 목록 검색
     */
    @GetMapping("/projects/{id}/users")
    public ResponseEntity<List<ProjectUserResponseDto>> findUsers(@PathVariable("id") Long id) {
        final List<ProjectUserResponseDto> response = projectService.findUsers(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트에서 유저(들) 삭제
     */
    @DeleteMapping("/projects/{id}/users")
    public ResponseEntity<Void> deleteUsers(@PathVariable("id") Long id, @RequestBody List<String> accountIds) {
        projectService.deleteUsersFromProject(id, accountIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 회원이 속한 프로젝트 목록 검색
     */
    @GetMapping("/users/{accountId}/projects")
    public ResponseEntity<List<UserProjectSummaryResponseDto>> findProjectsByAccountId(
            @PathVariable("accountId") String accountId) {
        final List<UserProjectSummaryResponseDto> response = projectService.findProjectsByAccountId(accountId);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트-회원 간 정보 검색
     */
    @GetMapping("/users/{accountId}/projects/{projectId}")
    public ResponseEntity<UserProjectAssociationResponseDto> findAssociationBetweenProjectAndUser(
            @PathVariable("accountId") String accountId,
            @PathVariable("projectId") Long projectId
    ) {
        final UserProjectAssociationResponseDto response = projectService.findAssociationBetweenProjectAndUser(
                accountId,
                projectId
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 프로젝트 접근시간 갱신
     */
    @PutMapping("/users/{accountId}/projects/{projectId}/access")
    public ResponseEntity<Void> renewAccessTime(
            @PathVariable("accountId") String accountId,
            @PathVariable("projectId") Long projectId
    ) {
        projectService.renewAccessTime(accountId, projectId);
        return ResponseEntity.ok().build();
    }

    /**
     * 즐겨찾기 갱신
     */
    @PutMapping("/users/{accountId}/projects/{projectId}/favorite")
    public ResponseEntity<Void> renewFavorite(
            @PathVariable("accountId") String accountId,
            @PathVariable("projectId") Long projectId,
            @RequestBody UserProjectFavoriteRequestDto userProjectFavoriteRequestDto
    ) {
        projectService.renewFavorite(accountId, projectId, userProjectFavoriteRequestDto);
        return ResponseEntity.ok().build();
    }
}
