package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.service.IssueImageService;
import com.ssssogong.issuemanager.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final IssueImageService issueImageService;

    // 이슈 생성
    @PostMapping("/projects/{projectId}/issues")
    public ResponseEntity<IssueIdResponseDto> create(@PathVariable("projectId") Long projectId, @ModelAttribute("imageFiles") IssueImageRequestDto issueImageRequestDto,
                                                     @RequestPart(value = "requestDto") IssueSaveRequestDto issueSaveRequestDto) throws IOException {
        IssueIdResponseDto issueIdResponseDto = issueService.save(projectId, issueSaveRequestDto);
        issueImageService.save(issueIdResponseDto.getIssueId(), issueImageRequestDto.getImageFiles());
        return ResponseEntity.ok(issueIdResponseDto);
    }

    // 이슈 확인
    @GetMapping("/projects/{projectId}/issues/{issueId}")
    public ResponseEntity<IssueShowResponseDto> show(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        IssueShowResponseDto issueShowResponseDTO = issueService.show(projectId, issueId);
        return ResponseEntity.ok(issueShowResponseDTO);
    }

    // 이슈 수정
    @PutMapping("/projects/{projectId}/issues/{issueId}")
    public ResponseEntity<IssueIdResponseDto> update(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,
                                                     @ModelAttribute("imageFiles") IssueImageRequestDto issueImageRequestDto, @RequestPart(value = "requestDto") IssueUpdateRequestDto issueUpdateRequestDto) throws IOException {
        issueImageService.delete(issueId);
        IssueIdResponseDto issueIdResponseDto = issueService.update(projectId, issueId, issueUpdateRequestDto);
        issueImageService.save(issueId, issueImageRequestDto.getImageFiles());
        return ResponseEntity.ok(issueIdResponseDto);
    }

    // 이슈 삭제
    @DeleteMapping("/projects/{projectId}/issues/{issueId}")
    public ResponseEntity<Void> delete(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        issueService.delete(projectId, issueId);
        return ResponseEntity.ok().build();
    }

    // 이슈 상태 변경
    @PostMapping("/projects/{projectId}/issues/{issueId}/state")
    public ResponseEntity<IssueIdResponseDto> stateUpdate(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,
                                                          @RequestPart(value = "requestDto") IssueStateUpdateRequestDto issueStateUpdateRequestDto) {
        IssueIdResponseDto issueIdResponseDto = issueService.stateUpdate(projectId, issueId, issueStateUpdateRequestDto);
        return ResponseEntity.ok(issueIdResponseDto);
    }


    // 프로젝트에 속한 이슈 목록 검색
    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<List<IssueProjectResponseDto>> search(
            @PathVariable("projectId") Long projectId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "reporter", required = false) String reporter,
            @RequestParam(value = "fixer", required = false) String fixer,
            @RequestParam(value = "assignee", required = false) String assignee,
            @RequestParam(value = "issueCount", required = false) Integer issueCount) {

        List<IssueProjectResponseDto> issues = issueService.findIssuesInProject(projectId, title, priority, state, category, reporter, fixer, assignee, issueCount);
        return ResponseEntity.ok(issues);
    }
}