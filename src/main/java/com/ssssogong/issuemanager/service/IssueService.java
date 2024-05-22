package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.mapper.IssueMapper;
import com.ssssogong.issuemanager.repository.IssueImageRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final IssueImageRepository issueImageRepository;
    private final IssueModificationService issueModificationService;
    private final IssueImageService issueImageService;

    // 이슈 생성
    @Transactional
    public IssueIdResponseDto save(Long projectId, IssueImageRequestDto issueImageRequestDto, IssueSaveRequestDto issueSaveRequestDto) throws IOException {

        // TODO 이슈 생성 시 Security에서 User를 가져와서 reporter 필드에 넣어야 함
//        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User reporter = userRepository.findByAccountId("tlsgusdn4818@gmail.com").orElseThrow(() -> new NotFoundException("해당 user가 없습니다."));
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));

        Issue issue = IssueMapper.convertToIssueSaveRequestDto(reporter, project, issueSaveRequestDto); // dto -> entity
        issueRepository.save(issue);    // issue 저장
        issueImageService.save(issue, issueImageRequestDto.getImageFiles());    // 이미지 저장

        return IssueMapper.convertToIssueIdResponseDto(issue);
    }

    //  이슈 확인
    @Transactional(readOnly = true)
    public IssueShowResponseDto show(Long projectId, Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        return IssueMapper.convertToIssueShowResponseDto(issue);    // entity -> dto
    }

    //  이슈 수정
    @Transactional
    public IssueIdResponseDto update(Long issueId, IssueImageRequestDto issueImageRequestDto, IssueUpdateRequestDto issueUpdateRequestDto) throws IOException {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        issueImageService.deleteInLocal(issue); // Local에서 이미지 삭제
        issueImageRepository.deleteByIssueId(issue.getId()); // DB에서 이미지 삭제
        IssueMapper.updateFromIssueUpdateRequestDto(issue, issueUpdateRequestDto);  // dto -> entity : update 처리
        issueRepository.save(issue);    // DB에 issue 저장(수정)
        issueImageService.save(issue, issueImageRequestDto.getImageFiles());    // DB에 이미지 저장(수정)
        return IssueMapper.convertToIssueIdResponseDto(issue);  // entity -> dto
    }

    //  이슈 삭제
    @Transactional
    public void delete(Long issueId) {
        if (!issueRepository.existsById(issueId))
            throw new NotFoundException("해당 issue가 없습니다.");
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        issueImageService.deleteInLocal(issue); // Local에서 이미지 삭제
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
        return IssueMapper.convertToIssueIdResponseDto(issue); // entity -> dto
    }

    // 프로젝트에 속한 이슈 검색
    @Transactional(readOnly = true)
    public List<IssueProjectResponseDto> findIssuesInProject(Long projectId, String title, String state, Integer issueCount) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));
        List<Issue> issues = issueRepository.findByProjectId(project.getId());  // 프로젝트에 속한 issue들을 꺼낸다.

        List<Issue> filteredIssues = issues.stream()
                .filter(issue -> title == null || issue.getTitle().contains(title)) // title이 없을 수도 있다. (필터링 X)
                .filter(issue -> state == null || issue.getState().toString().equals(state))    // state가 없을 수도 있다. (필터링 X)
                .limit(issueCount != null ? issueCount : issues.size()) // issueCount가 지정되지 않았을 경우 모든 이슈 반환
                .toList();
        return filteredIssues.stream()
                .map(IssueMapper::convertToIssueProjectResponseDto) // entity -> dto
                .collect(Collectors.toList());
    }
}