package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public Project findOne(Long projectId) {
        return projectRepository.findProject(projectId);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }
}
