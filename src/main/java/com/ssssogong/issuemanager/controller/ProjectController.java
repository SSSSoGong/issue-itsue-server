package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.ProjectCreationRequest;
import com.ssssogong.issuemanager.dto.ProjectIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectController {

    @PostMapping("/projects")
    public ResponseEntity<ProjectIdResponse> create(@RequestBody ProjectCreationRequest projectCreationRequest) {
        final ProjectIdResponse response = new ProjectIdResponse(-1L);
        return ResponseEntity.ok(response);
    }
}
