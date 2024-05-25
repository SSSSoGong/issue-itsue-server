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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("@ProjectPrivilegeEvaluator.hasPrivilege(#projectId, @Privilege.ISSUE_REPORTABLE)")
    @Transactional
    public IssueIdResponseDto save(Long projectId, IssueSaveRequestDto issueSaveRequestDto) throws IOException {

        // TODO : security에서 reporter 가져오기
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User reporter = userRepository.findByAccountId(accountId).orElseThrow(() -> new NotFoundException("해당 user가 없습니다."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));

        Issue issue = IssueMapper.toIssueFromSaveRequestDto(reporter, project, issueSaveRequestDto); // dto -> entity
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
    @PreAuthorize("@ProjectPrivilegeEvaluator.hasPrivilege(#projectId, @Privilege.ISSUE_UPDATABLE)")
    @Transactional
    public IssueIdResponseDto update(Long projectId, Long issueId, IssueUpdateRequestDto issueUpdateRequestDto) throws IOException {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        if (!issue.getReporter().getAccountId().equals(SecurityContextHolder.getContext().getAuthentication().getName())) // issue에 대한 reporter 당사자가 수정 가능
            throw new AccessDeniedException("You are not authorized to update this issue");
        else {
            issue.update(issueUpdateRequestDto.getTitle(), issueUpdateRequestDto.getDescription(), issueUpdateRequestDto.getPriority());
            return IssueMapper.toIssueIdResponseDto(issue);  // entity -> dto
        }
    }

    //  이슈 삭제
    @PreAuthorize("@ProjectPrivilegeEvaluator.hasPrivilege(#projectId, @Privilege.ISSUE_DELETABLE)")
    @Transactional
    public void delete(Long projectId, Long issueId) {
        if (!issueRepository.existsById(issueId))
            throw new NotFoundException("해당 issue가 없습니다.");
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        issueRepository.deleteById(issueId);    // cascade 때문에 DB에서 연관된 issueModification, issueImage 자동 삭제
    }

    //  이슈 상태 변경
    @PreAuthorize("@ProjectPrivilegeEvaluator.canChangeIssueState(#issueId, #issueStateUpdateRequestDto)")
    @Transactional
    public IssueIdResponseDto stateUpdate(Long projectId, Long issueId, IssueStateUpdateRequestDto issueStateUpdateRequestDto) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        State from = issue.getState();
        issue.update(issueStateUpdateRequestDto.getState());
        State to = issue.getState();

        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User modifier = userRepository.findByAccountId(accountId).orElseThrow(() -> new NotFoundException("해당 user가 없습니다."));

        if (to.equals(State.ASSIGNED)) { // new -> assigned : modifier = PL, assignee = dev
            if (issueStateUpdateRequestDto.getAssignee() != null && !issueStateUpdateRequestDto.getAssignee().isBlank()) {  // new -> assigned : modifier = PL, assignee = dev
                User assignee = userRepository.findByAccountId(issueStateUpdateRequestDto.getAssignee()).orElseThrow(() -> new NotFoundException("해당 user가 없습니다"));
                issue.setAssignee(assignee);
            }
        }
        if (to.equals(State.FIXED)) { // assigned -> fixed : modifier = dev, fixer = dev
            if (issue.getAssignee().getAccountId().equals(modifier.getAccountId())) // issue의 assignee만 FIXED 가능
                issue.setFixer(modifier);
            else
                throw new AccessDeniedException("You are not authorized to update the state for this issue");
        }
        if (to.equals(State.RESOLVED) || to.equals(State.REOPENED)) { // fixed -> resolved : modifier = tester, issue의 reporter만 RESOLVED 가능
            if (!issue.getAssignee().getAccountId().equals(modifier.getAccountId()))
                throw new AccessDeniedException("You are not authorized to update the state for this issue");
        }
        if(to.equals(State.CLOSED))
            // TODO : issue가 속해 있는 Project의 PL만 가능

        issueModificationService.save(issue, from, to, modifier); // issueModification 저장
        return IssueMapper.toIssueIdResponseDto(issue); // entity -> dto
    }

    // 프로젝트에 속한 이슈 검색
    @Transactional(readOnly = true)
    public List<IssueProjectResponseDto> findIssuesInProject(Long projectId, String title, String priority, String state, String category, String reporter, String fixer, String assignee, Integer issueCount) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));
        List<Issue> issues = issueRepository.findByProjectId(project.getId());  // 프로젝트에 속한 issue들을 꺼낸다.

        List<Issue> filteredIssues = issues.stream()
                .sorted(Comparator.comparing(Issue::getUpdatedAt).reversed()) // updatedAt 기준으로 내림차순 정렬
                .filter(issue -> title == null || issue.getTitle().contains(title)) // title 검색 (부분일치)
                .filter(issue -> priority == null || priority.isBlank() || issue.getPriority().toString().equals(priority))  // priority 검색
                .filter(issue -> state == null || state.isBlank() || issue.getState().toString().equals(state))    // state 검색
                .filter(issue -> category == null || category.isBlank() || issue.getCategory().toString().equals(category)) // category 검색
                .filter(issue -> reporter == null || reporter.isBlank() || (issue.getReporter() != null && issue.getReporter().getAccountId().contains(reporter))) // reporter 검색
                .filter(issue -> fixer == null || fixer.isBlank() || (issue.getFixer() != null && issue.getFixer().getAccountId().contains(fixer))) // fixer 검색
                .filter(issue -> assignee == null || assignee.isBlank() || (issue.getAssignee() != null && issue.getAssignee().getAccountId().contains(assignee))) // assignee 검색
                .limit(issueCount != null ? issueCount : issues.size()) // issueCount가 지정되지 않았을 경우 모든 이슈 반환
                .toList();
        return filteredIssues.stream()
                .map(IssueMapper::toIssueProjectResponseDto) // entity -> dto
                .collect(Collectors.toList());
    }
}