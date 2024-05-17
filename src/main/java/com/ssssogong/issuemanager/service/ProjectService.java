package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.UserProject;
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

    public ProjectIdResponse create(final ProjectCreationRequest projectCreationRequest) {
        //todo: admin 권한 체크
        final Project project = Project.builder()
                .admin(null) //todo
                .name(projectCreationRequest.getName())
                .subject(projectCreationRequest.getSubject())
                .build();
        return new ProjectIdResponse(projectRepository.save(project).getId());
    }

    public ProjectDetailsResponse findById(final Long id) {
        final Project project = projectRepository.findById(id).orElseThrow();
        return ProjectDetailsResponse.from(project);
    }

    public ProjectIdResponse updateById(final Long id, final ProjectUpdateRequest projectUpdateRequest) {
        final Project project = projectRepository.findById(id).orElseThrow();
        project.update(projectUpdateRequest.getName(), projectUpdateRequest.getSubject());
        return new ProjectIdResponse(id);
    }

    public ProjectIdResponse deleteById(final Long id) {
        final Project project = projectRepository.findById(id).orElseThrow();
        //todo: project.checkAdmin() 프로젝트를 생성한 admin인지 체크
        projectRepository.deleteById(id);
        return new ProjectIdResponse(id);
    }

    public void addUsersToProject(final Long projectId, final List<ProjectUserAdditionRequest> request) {
        final Project project = projectRepository.findById(projectId).orElseThrow();
        //todo: 유저 중복 참여X
        final List<Role> roles = roleRepository.findAll();
        //todo: project.checkAdmin() 프로젝트를 생성한 admin인지 체크
        List<UserProject> userProjects = new ArrayList<>();
        for (ProjectUserAdditionRequest userData : request) {
            final User user = userRepository.findByAccountId(userData.getAccountId()).orElseThrow();
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

    public List<UserProjectSummaryResponse> findProjectsByAccountId(final String accountId) {
        final List<UserProject> userProjects = userProjectRepository.findAllByAccountId(accountId);
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
        final UserProject userProject = userProjectRepository.findByAccountIdAndProjectId(accountId, projectId)
                .orElseThrow();
        return new UserProjectAssociationResponse(
                userProject.getRole().getRoleName(),
                userProject.getProject().getCreatedAt().toString(),
                userProject.getAccessTime().toString()
        );
    }

    public void renewAccessTime(final String accountId, final String projectId) {
        UserProject userProject = userProjectRepository.findByAccountIdAndProjectId(accountId, projectId).orElseThrow();
        userProject.updateAccessTime();
    }
}
