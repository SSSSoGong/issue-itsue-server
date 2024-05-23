package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.service.IssueImageService;
import com.ssssogong.issuemanager.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                                       @RequestPart(value = "requestDto") IssueSaveRequestDto issueSaveRequestDto) {
        try {
            IssueIdResponseDto issueIdResponseDto = issueService.save(projectId, issueSaveRequestDto);
            issueImageService.save(issueIdResponseDto.getIssueId(), issueImageRequestDto.getImageFiles());
            return new ResponseEntity<>(issueIdResponseDto, HttpStatus.OK);
        } catch (NotFoundException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 이슈 확인
    @GetMapping("/projects/{projectId}/issues/{issueId}")
    public ResponseEntity<IssueShowResponseDto> show(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        try {
            IssueShowResponseDto issueShowResponseDTO = issueService.show(projectId, issueId);
            return new ResponseEntity<>(issueShowResponseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 수정
    @PutMapping("/projects/{projectId}/issues/{issueId}")
    public ResponseEntity<IssueIdResponseDto> update(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,
                                       @ModelAttribute("imageFiles") IssueImageRequestDto issueImageRequestDto, @RequestPart(value = "requestDto") IssueUpdateRequestDto issueUpdateRequestDto) {
        try {
            issueImageService.delete(issueId);
            IssueIdResponseDto issueIdResponseDto = issueService.update(issueId, issueUpdateRequestDto);
            issueImageService.save(issueId, issueImageRequestDto.getImageFiles());
            return new ResponseEntity<>(issueIdResponseDto, HttpStatus.OK);
        } catch (NotFoundException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 삭제
    @DeleteMapping("/projects/{projectId}/issues/{issueId}")
    public ResponseEntity<Void> delete(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        try {
            issueService.delete(issueId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 상태 변경
    @PostMapping("/projects/{projectId}/issues/{issueId}/state")
    public ResponseEntity<IssueIdResponseDto> stateUpdate(@PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId,
                                            @RequestPart(value = "requestDto") IssueStateUpdateRequestDto issueStateUpdateRequestDto) {
        try {
            IssueIdResponseDto issueIdResponseDto = issueService.stateUpdate(issueId, issueStateUpdateRequestDto);
            return new ResponseEntity<>(issueIdResponseDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }


    // 프로젝트에 속한 이슈 목록 검색
    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<List<IssueProjectResponseDto>> search(
            @PathVariable("projectId") Long projectId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "priority", required = false) String priority,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "issueCount", required = false) Integer issueCount) {

        try {
            List<IssueProjectResponseDto> issues = issueService.findIssuesInProject(projectId, title, priority, state, category, issueCount);
            return new ResponseEntity<>(issues, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
}