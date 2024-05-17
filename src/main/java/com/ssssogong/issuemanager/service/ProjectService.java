package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectIdResponse;
import com.ssssogong.issuemanager.dto.ProjectUpdateRequest;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

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
}
