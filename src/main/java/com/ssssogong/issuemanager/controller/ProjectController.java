package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectDetailsResponse;
import com.ssssogong.issuemanager.dto.ProjectIdResponse;
import com.ssssogong.issuemanager.dto.ProjectUpdateRequest;
import com.ssssogong.issuemanager.dto.ProjectUserAdditionRequest;
import com.ssssogong.issuemanager.dto.ProjectUserResponse;
import com.ssssogong.issuemanager.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<ProjectDetailsResponse> findById(@PathVariable("id") Long id) {
        final ProjectDetailsResponse response = projectService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectIdResponse> updateById(@PathVariable("id") Long id,
                                                        @RequestBody ProjectUpdateRequest projectUpdateRequest) {
        final ProjectIdResponse response = projectService.updateById(id, projectUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<ProjectIdResponse> deleteById(@PathVariable("id") Long id) {
        final ProjectIdResponse response = projectService.deleteById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/projects/{id}/users")
    public ResponseEntity<Void> addUsers(@PathVariable("id") Long id, @RequestBody List<ProjectUserAdditionRequest> request) {
        projectService.addUsersToProject(id, request);
        return ResponseEntity.ok().build();
    }


    @GetMapping("projects/{id}/users")
    public ResponseEntity<List<ProjectUserResponse>> findUsers(@PathVariable("id") Long id) {
        final List<ProjectUserResponse> response =  projectService.findUsers(id);
        return ResponseEntity.ok(response);
    }
}
