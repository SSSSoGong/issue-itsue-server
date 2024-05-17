package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectIdResponse;
import com.ssssogong.issuemanager.dto.ProjectUpdateRequest;
import com.ssssogong.issuemanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectIdResponse> create(@RequestBody ProjectCreationRequest projectCreationRequest) {
        final ProjectIdResponse response = projectService.create(projectCreationRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDetailsResponse> findById(@PathVariable Long id) {
        final ProjectDetailsResponse response = projectService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectIdResponse> updateById(@PathVariable Long id, @RequestBody ProjectUpdateRequest projectUpdateRequest) {
        final ProjectIdResponse response = projectService.updateById(id, projectUpdateRequest);
        return ResponseEntity.ok(response);
    }
}
