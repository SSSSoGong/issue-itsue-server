package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
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

    // 이슈 생성
    @PostMapping("/issues")
    public ResponseEntity<Long> create(@ModelAttribute("imageFiles") IssueImageRequestDto issueImageRequestDto, @RequestPart(value = "requestDto") IssueSaveRequestDto issueSaveRequestDto) {
        try {
            Long issueId = issueService.save(issueImageRequestDto, issueSaveRequestDto);
            return new ResponseEntity<>(issueId, HttpStatus.OK);
        } catch (NotFoundException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 이슈 확인
    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueResponseDto> show(@PathVariable("id") Long id) {
        try {
            IssueResponseDto issueResponseDTO = issueService.show(id);
            return new ResponseEntity<>(issueResponseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 수정
    @PutMapping("/issues/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long id, @ModelAttribute("imageFiles") IssueImageRequestDto issueImageRequestDto, @RequestPart(value = "requestDto") IssueUpdateRequestDto requestDto) {
        try {
            Long updateIssueId = issueService.update(id, issueImageRequestDto, requestDto);
            return new ResponseEntity<>(updateIssueId, HttpStatus.OK);
        } catch (NotFoundException | IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 삭제
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            issueService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 상태 변경
    @PostMapping("/issues/{id}/state")
    public ResponseEntity<Void> stateUpdate(@PathVariable("id") Long id, @RequestPart(value = "requestDto") IssueStateUpdateRequestDto requestDto) {
        try {
            issueService.stateUpdate(id, requestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }


    // 프로젝트에 속한 이슈 목록 검색
    @GetMapping("/projects/{pid}/issues")
    public ResponseEntity<List<IssueProjectResponseDto>> search(
            @PathVariable("pid") Long projectId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "issueCount", required = false) Integer issueCount) {

        try {
            List<IssueProjectResponseDto> issues = issueService.findIssuesInProject(projectId, title, state, issueCount);
            return new ResponseEntity<>(issues, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
}