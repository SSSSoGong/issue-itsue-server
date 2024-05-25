package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.Roles;
import com.ssssogong.issuemanager.domain.UserProject;
import com.ssssogong.issuemanager.domain.UserProjectSorter;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.Administrator;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.mapper.ProjectMapper;
import com.ssssogong.issuemanager.mapper.UserProjectMapper;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public ProjectIdResponseDto create(final String adminAccountId, final ProjectCreationRequestDto projectCreationRequestDto) {
        final User admin = userRepository.findByAccountId(adminAccountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 어드민 계정을 찾을 수 없음"));
        final Project project = Project.builder()
                .admin(admin)
                .name(projectCreationRequestDto.getName())
                .subject(projectCreationRequestDto.getSubject())
                .build();
        projectRepository.save(project);
        final UserProject userProject = UserProject.builder()
                .project(project)
                .user(admin)
                .role(roleRepository.findByName("Administrator").orElseThrow(() -> new IllegalArgumentException("어드민 role을 찾을 수 없음")))
                .build();
        userProjectRepository.save(userProject);
        return ProjectMapper.toProjectIdResponse(project.getId());
    }

    @Transactional(readOnly = true)
    public ProjectDetailsResponseDto findById(final Long id) {
        final Project project = findProjectById(id);
        return ProjectMapper.toProjectDetailsResponse(project);
    }

    private Project findProjectById(final Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없음"));
    }

    @Transactional
    public ProjectIdResponseDto updateById(final Long id, final ProjectUpdateRequestDto projectUpdateRequestDto) {
        final Project project = findProjectById(id);
        project.update(projectUpdateRequestDto.getName(), projectUpdateRequestDto.getSubject());
        return ProjectMapper.toProjectIdResponse(project.getId());
    }

    @Transactional
    public ProjectIdResponseDto deleteById(final Long id) {
        userProjectRepository.deleteAllByProjectId(id);
        projectRepository.deleteById(id);
        return ProjectMapper.toProjectIdResponse(id);
    }

    @Transactional
    public void addUsersToProject(final Long projectId, final List<ProjectUserAdditionRequestDto> request) {
        checkDuplicatedAddition(projectId, request);

        final Project project = findProjectById(projectId);
        final Roles roles = Roles.builder().roles(roleRepository.findAll()).build();

        List<UserProject> userProjects = new ArrayList<>();
        for (ProjectUserAdditionRequestDto userData : request) {
            userProjects.add(
                    UserProject.builder()
                            .user(findUserByAccountId(userData.getAccountId()))
                            .project(project)
                            .role(roles.findRole(userData.getRole()))
                            .build()
            );
        }
        userProjectRepository.saveAll(userProjects);
    }

    private User findUserByAccountId(final String accountId) {
        return userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없음"));
    }

    private void checkDuplicatedAddition(final Long projectId, final List<ProjectUserAdditionRequestDto> request) {
        final List<String> accountIds = request.stream()
                .map(ProjectUserAdditionRequestDto::getAccountId)
                .toList();
        if (userProjectRepository.existsByProjectIdAndAccountIdIn(projectId, accountIds)) {
            throw new IllegalArgumentException("이미 프로젝트에 참여중인 유저가 포함되어 있음");
        }
    }

    @Transactional(readOnly = true)
    public List<ProjectUserResponseDto> findUsers(final Long id) {
        List<UserProject> userProjects = userProjectRepository.findAllByProjectId(id)
                .stream()
                .filter(each -> !each.getRole().isRole("Administrator")).toList();
        return UserProjectMapper.toProjectUserResponse(userProjects);
    }

    @Transactional
    public void deleteUsersFromProject(final Long projectId, final List<String> accountIds) {
        userProjectRepository.deleteAllByProjectIdAndAccountIdIn(projectId, accountIds);
    }

    @Transactional(readOnly = true)
    public List<UserProjectSummaryResponseDto> findProjectsByAccountId(final String accountId) {
        final List<UserProject> userProjects = userProjectRepository.findAllByAccountId(accountId);
        final List<UserProject> sortedUserProjects = UserProjectSorter.sortByAccessTimeAndIsFavorite(userProjects);
        return UserProjectMapper.toUserProjectSummaryResponse(sortedUserProjects);
    }

    @Transactional(readOnly = true)
    public UserProjectAssociationResponseDto findAssociationBetweenProjectAndUser(final String accountId,
                                                                                  final long projectId) {
        final UserProject userProject = findUserProjectByAccountIdAndProjectId(accountId, projectId);
        return UserProjectMapper.toUserProjectAssociationResponse(userProject);
    }

    private UserProject findUserProjectByAccountIdAndProjectId(final String accountId, final long projectId) {
        return userProjectRepository.findByAccountIdAndProjectId(accountId, projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트를 찾을 수 없음"));
    }

    @Transactional
    public void renewAccessTime(final String accountId, final long projectId) {
        UserProject userProject = findUserProjectByAccountIdAndProjectId(accountId, projectId);
        userProject.updateAccessTime();
    }

    @Transactional
    public void renewFavorite(
            final String accountId,
            final Long projectId,
            final UserProjectFavoriteRequestDto userProjectFavoriteRequestDto) {
        final UserProject userProject = findUserProjectByAccountIdAndProjectId(accountId, projectId);
        userProject.updateIsFavorite(userProjectFavoriteRequestDto.isFavorite());
    }
}
