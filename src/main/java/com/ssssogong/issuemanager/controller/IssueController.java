package com.ssssogong.issuemanager.controller;

import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    // 이슈 생성
    @PostMapping("/issues")
    public ResponseEntity<Long> create(@RequestParam MultipartFile file, @RequestParam IssueSaveRequestDto requestDto) {
        // tester 확인 ... 아니면 403
        // SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    // 이슈 확인
    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueDTO> check(@PathVariable("id") Long id) {
        try {
            IssueDTO issueDTO = issueService.check(id);
            return new ResponseEntity<>(issueDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 수정
    @PutMapping("/issues/{id}")
    public ResponseEntity<Long> update(@PathVariable("id") Long id, @RequestBody IssueUpdateRequestDto requestDto) {
        // tester 확인... 아니면 403
        // SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        try {
            Long updateIssueId = issueService.update(id, requestDto);
            return new ResponseEntity<>(updateIssueId, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 삭제
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        // admin 확인 ... 아니면 403
        // SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        try {
            issueService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }

    // 이슈 상태 변경
    @PostMapping("/issues/{id}/state")
    public ResponseEntity<Void> stateUpdate(@PathVariable("id") Long id, @RequestBody IssueStateUpdateRequestDto requestDto) {
        // 상태 변경에 적합한 권한을 가지고 있는지 ... 아니면 403
        // SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        try {
            issueService.stateUpdate(id, requestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }


    // 프로젝트에 속한 이슈 목록 검색
    @GetMapping("/projects/{pid}/issues")
    public ResponseEntity<List<IssueProjectDto>> search(
            @PathVariable("pid") Long projectId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Integer issueCount) {

        try {
            List<IssueProjectDto> issues = issueService.findIssuesInProject(projectId, title, state, issueCount);
            return new ResponseEntity<>(issues, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
    }
}