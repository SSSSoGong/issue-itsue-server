package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;

    // 이슈 생성
    @Transactional
    public Long save(IssueSaveRequestDto requestDto) {
        // 토큰에서 User 정보 꺼내기
        // projectService에서 project 가져오기
        Issue issue = Issue.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .priority(Priority.valueOf(requestDto.getPriority()))
                .state(State.NEW)
                .category(Category.valueOf(requestDto.getCategory()))
                .reporter("토큰에서 추출한 User 정보")
                .project("projectService에서 project 가져오기")
                .imageUrls()
                .build();

        return issue.getId();
    }

    //  이슈 확인
    @Transactional(readOnly = true)
    public IssueDTO check(Long issueId) {
        return issueRepository.findById(issueId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
    }

    //  이슈 수정
    @Transactional
    public Long update(Long issueId, IssueUpdateRequestDto requestDto) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        issue.update(requestDto.getTitle(), requestDto.getDescription(), requestDto.getPriority());
        return issue.getId();
    }

    //  이슈 삭제
    @Transactional
    public void delete(Long issueId) {
        if (!issueRepository.existsById(issueId))
            throw new NotFoundException("해당 issue가 없습니다.");
        issueRepository.deleteById(issueId);
    }

    //  이슈 상태 변경
    @Transactional
    public void stateUpdate(Long issueId, IssueStateUpdateRequestDto requestDto) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
//      userService에서 requestDto.getAssginee()를 이용해 assignee(accountId)를 가진 User를 return받아서 stateUpdate의 parameter로 전달
//      만약에 assignee가 존재하지 않으면 null 전달
//      issue.stateUpdate(requestDto.getState(), User assignee);
    }

    // 프로젝트에 속한 이슈 검색
    @Transactional(readOnly = true)
    public List<IssueProjectDto> findIssuesInProject(Long projectId, String title, String state, Integer issueCount) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));
        List<IssueProjectDto> issues = issueRepository.findIssuesByProjectId(projectId, title, state);
        if (issueCount != null && issueCount < issues.size()) {
            return issues.subList(0, issueCount);
        }
        return issues;
    }

    public IssueDTO convertToDTO(Issue issue) {
        return IssueDTO.builder()
                .title(issue.getTitle())
                .description(issue.getDescription())
                .priority(issue.getPriority())
                .state(issue.getState())
                .category(issue.getCategory())
                .reporter(issue.getReporter())
                .reportedDate(issue.getCreatedAt())
                .build();
    }

//    public Issue convertToEntity(IssueDTO issueDTO) {
//        User reporter = userRepository.findByUsername(issueDTO.getReporter()).orElseThrow(() ->
//                new RuntimeException("User not found"));
//
//        return Issue.builder()
//                .title(issueDTO.getTitle())
//                .description(issueDTO.getDescription())
//                .priority(issueDTO.getPriority())
//                .state(issueDTO.getState())
//                .category(issueDTO.getCategory())
//                .reporter(reporter)
//                .createdAt(issueDTO.getReportedDate())
//                .build();
//    }
}
