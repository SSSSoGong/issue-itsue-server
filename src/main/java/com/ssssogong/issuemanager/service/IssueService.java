package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.mapper.IssueMapper;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final IssueModificationService issueModificationService;

    // 이슈 생성
    @Transactional
    public IssueIdResponseDto save(Long projectId, IssueSaveRequestDto issueSaveRequestDto) throws IOException {

        // TODO 이슈 생성 시 Security에서 User를 가져와서 reporter 필드에 넣어야 함
//        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User reporter = userRepository.findByAccountId("tlsgusdn4818@gmail.com").orElseThrow(() -> new NotFoundException("해당 user가 없습니다."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));

        Issue issue = IssueMapper.toIssueSaveRequestDto(reporter, project, issueSaveRequestDto); // dto -> entity
        issueRepository.save(issue);    // issue 저장
        return IssueMapper.toIssueIdResponseDto(issue);
    }

    //  이슈 확인
    @Transactional(readOnly = true)
    public IssueShowResponseDto show(Long projectId, Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        return IssueMapper.toIssueShowResponseDto(issue);    // entity -> dto
    }

    //  이슈 수정
    @Transactional
    public IssueIdResponseDto update(Long issueId, IssueUpdateRequestDto issueUpdateRequestDto) throws IOException {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        IssueMapper.updateFromIssueUpdateRequestDto(issue, issueUpdateRequestDto);  // dto -> entity : update 처리
        issueRepository.save(issue);    // DB에 issue 저장(수정)
        return IssueMapper.toIssueIdResponseDto(issue);  // entity -> dto
    }

    //  이슈 삭제
    @Transactional
    public void delete(Long issueId) {
        if (!issueRepository.existsById(issueId))
            throw new NotFoundException("해당 issue가 없습니다.");
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        issueRepository.deleteById(issueId);    // cascade 때문에 DB에서 연관된 issueModification, issueImage 자동 삭제
    }

    //  이슈 상태 변경
    @Transactional
    public IssueIdResponseDto stateUpdate(Long issueId, IssueStateUpdateRequestDto issueStateUpdateRequestDto) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        State from = issue.getState();
        IssueMapper.updateFromIssueStateUpdateRequestDto(issue, issueStateUpdateRequestDto);    // dto -> entity : update 처리
        State to = issue.getState();
        if (!issueStateUpdateRequestDto.getAssignee().isBlank()) {  // assignee가 없으면 저장 X
            User assignee = userRepository.findByAccountId(issueStateUpdateRequestDto.getAssignee()).orElseThrow(() -> new NotFoundException("해당 user가 없습니다"));
            issue.setAssignee(assignee);
        }
        issueRepository.save(issue);    // issue 저장(수정)
        issueModificationService.save(issue, from, to); // issueModification 저장
        return IssueMapper.toIssueIdResponseDto(issue); // entity -> dto
    }

    // 프로젝트에 속한 이슈 검색
    @Transactional(readOnly = true)
    public List<IssueProjectResponseDto> findIssuesInProject(Long projectId, String title, String priority, String state, String category, Integer issueCount) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));
        List<Issue> issues = issueRepository.findByProjectId(project.getId());  // 프로젝트에 속한 issue들을 꺼낸다.

        List<Issue> filteredIssues = issues.stream()
                .sorted(Comparator.comparing(Issue::getUpdatedAt).reversed()) // updatedAt 기준으로 내림차순 정렬
                .filter(issue -> title == null || issue.getTitle().contains(title)) // title이 없을 수도 있다. (필터링 X)
                .filter(issue -> priority == null || priority.isBlank() || issue.getPriority().toString().equals(priority))  // priority가 없을 수도 있다. (필터링 X)
                .filter(issue -> state == null || state.isBlank() || issue.getState().toString().equals(state))    // state가 없을 수도 있다. (필터링 X)
                .filter(issue -> category == null || category.isBlank() || issue.getCategory().toString().equals(category)) // category가 없을 수도 있다. (필터링 X)
                .limit(issueCount != null ? issueCount : issues.size()) // issueCount가 지정되지 않았을 경우 모든 이슈 반환
                .toList();
        return filteredIssues.stream()
                .map(IssueMapper::toIssueProjectResponseDto) // entity -> dto
                .collect(Collectors.toList());
    }
}