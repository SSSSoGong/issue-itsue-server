package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Role;
import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectIdResponse;
import com.ssssogong.issuemanager.dto.ProjectUpdateRequest;
import com.ssssogong.issuemanager.dto.ProjectUserAdditionRequest;
import com.ssssogong.issuemanager.dto.ProjectUserResponse;
import com.ssssogong.issuemanager.dto.UserProjectAssociationResponse;
import com.ssssogong.issuemanager.dto.UserProjectSummaryResponse;
import com.ssssogong.issuemanager.mapper.ProjectMapper;
import com.ssssogong.issuemanager.repository.AdminRepository;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;

    public ProjectIdResponse create(final String adminAccountId, final ProjectCreationRequest projectCreationRequest) {
        final Admin admin = adminRepository.findByAccountId(adminAccountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 어드민 계정을 찾을 수 없음"));
        final Project project = Project.builder()
                .admin(admin)
                .name(projectCreationRequest.getName())
                .subject(projectCreationRequest.getSubject())
                .build();
        projectRepository.save(project);
        return ProjectMapper.toProjectIdResponse(project.getId());
    }

    public ProjectDetailsResponse findById(final Long id) {
        final Project project = findProjectById(id);
        return ProjectMapper.toProjectDetailsResponse(project);
    }

    private Project findProjectById(final Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없음"));
    }

    public ProjectIdResponse updateById(final Long id, final ProjectUpdateRequest projectUpdateRequest) {
        final Project project = findProjectById(id);
        project.update(projectUpdateRequest.getName(), projectUpdateRequest.getSubject());
        return ProjectMapper.toProjectIdResponse(project.getId());
    }

    public ProjectIdResponse deleteById(final Long id) {
        //todo: 연관된 userProject.. issue... comment.. issuemodification 등등 삭제해야 함
        projectRepository.deleteById(id);
        return ProjectMapper.toProjectIdResponse(id);
    }

    public void addUsersToProject(final Long projectId, final List<ProjectUserAdditionRequest> request) {
        final Project project = findProjectById(projectId);
        //todo: 유저 중복 참여X
        final List<Role> roles = roleRepository.findAll();
        List<UserProject> userProjects = new ArrayList<>();
        for (ProjectUserAdditionRequest userData : request) {
            final User user = userRepository.findByAccountId(userData.getAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없음"));
            final UserProject userProject = UserProject.builder()
                    .user(user)
                    .project(project)
                    .role(findRole(roles, userData))
                    .build();
            userProjects.add(userProject);
        }
        userProjectRepository.saveAll(userProjects);
    }

    private Role findRole(final List<Role> roles, final ProjectUserAdditionRequest userData) {
        return roles.stream()
                .filter(each -> each.isRole(userData.getRole()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("역할을 찾지 못함"));
    }

    public List<ProjectUserResponse> findUsers(final Long id) {
        List<UserProject> userProjects = userProjectRepository.findAllByProjectId(id);
        return userProjects.stream()
                .map(each -> new ProjectUserResponse(
                        each.getUser().getAccountId(),
                        each.getUser().getUsername(),
                        each.getRole().getRoleName()
                )).toList();
    }

    public void deleteUsersFromProject(final Long projectId, final List<String> accountIds) {
        userProjectRepository.deleteAllByProjectIdAndAccountIdIn(projectId, accountIds);
    }

    public List<UserProjectSummaryResponse> findProjectsByAccountId(final String accountId) {
        final List<UserProject> userProjects = userProjectRepository.findAllByAccountId(accountId);
        //TODO : 최근에 접속한 프로젝트 순으로 정렬합니다.
        //todo: 즐겨찾기?
        return userProjects.stream()
                .map(each -> new UserProjectSummaryResponse(
                        each.getProject().getId(),
                        each.getProject().getName(),
                        each.getProject().getCreatedAt().toString(),
                        each.isFavorite()
                )).toList();
    }

    public UserProjectAssociationResponse findAssociationBetweenProjectAndUser(final String accountId,
                                                                               final String projectId) {
        final UserProject userProject = findUserProjectByAccountIdAndProjectId(accountId, projectId);
        return new UserProjectAssociationResponse(
                userProject.getRole().getRoleName(),
                userProject.getProject().getCreatedAt().toString(),
                userProject.getAccessTime().toString()
        );
    }

    private UserProject findUserProjectByAccountIdAndProjectId(final String accountId, final String projectId) {
        return userProjectRepository.findByAccountIdAndProjectId(accountId, projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없음"));
    }

    public void renewAccessTime(final String accountId, final String projectId) {
        UserProject userProject = findUserProjectByAccountIdAndProjectId(accountId, projectId);
        userProject.updateAccessTime();
    }
}
