package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueImage;
import com.ssssogong.issuemanager.domain.IssueModification;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.dto.*;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.mapper.IssueMapper;
import com.ssssogong.issuemanager.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final IssueImageRepository issueImageRepository;
    private final IssueModificationRepository issueModificationRepository;

    // 이슈 생성
    @Transactional
    public Long save(IssueImageRequestDto issueImageRequestDto, IssueSaveRequestDto issueSaveRequestDto) throws IOException {

//        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();
        User reporter = userRepository.findByAccountId("tlsgusdn4818@gmail.com").orElseThrow(() -> new NotFoundException("해당 user가 없습니다."));
        Project project = projectRepository.findById(issueSaveRequestDto.getProjectId()).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));

        Issue issue = IssueMapper.convertToIssueSaveRequestDto(reporter, project, issueSaveRequestDto);
        issueRepository.save(issue);
        saveImages(issue, issueImageRequestDto.getImageFiles());

        return issue.getId();
    }

    //  이슈 확인
    @Transactional(readOnly = true)
    public IssueResponseDto show(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        return IssueMapper.convertToIssueResponseDto(issue);
    }

    //  이슈 수정
    @Transactional
    public Long update(Long issueId, IssueImageRequestDto issueImageRequestDto, IssueUpdateRequestDto requestDto) throws IOException {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
        issueImageRepository.deleteByIssueId(issue.getId());
        IssueMapper.updateFromIssueUpdateRequestDto(issue, requestDto);
        issueRepository.save(issue);
        saveImages(issue, issueImageRequestDto.getImageFiles());
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
        State from = issue.getState();
        IssueMapper.updateFromIssueStateUpdateRequestDto(issue, requestDto);
        State to = issue.getState();
        if (!requestDto.getAssignee().isBlank()) {
            User assignee = userRepository.findByUsername(requestDto.getAssignee()).orElseThrow(() -> new NotFoundException("해당 user가 없습니다"));
            issue.setAssignee(assignee);
        }
        issueRepository.save(issue);
        saveIssueModifications(issue, from, to);
    }

    // 프로젝트에 속한 이슈 검색
    @Transactional(readOnly = true)
    public List<IssueProjectResponseDto> findIssuesInProject(Long projectId, String title, String state, Integer issueCount) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException("해당 project가 없습니다"));
        List<Issue> issues = issueRepository.findByProjectId(project.getId());
        List<Issue> filteredIssues = issues.stream()
                .filter(issue -> title == null || issue.getTitle().contains(title))
                .filter(issue -> state == null || issue.getState().toString().equals(state))
                .limit(issueCount != null ? issueCount : issues.size()) // issueCount가 지정되지 않았을 경우 모든 이슈 반환
                .toList();

        return filteredIssues.stream()
                .map(IssueMapper::convertToIssueProjectResponseDto)
                .collect(Collectors.toList());
    }

    private void saveImages(Issue issue, List<MultipartFile> imageFiles) throws IOException {

        Path currentPath = Paths.get("").toAbsolutePath();  // 현재 작업 절대경로
        Path saveImagesPath = currentPath.resolve("saveimages"); // 현재 경로에 save_images 경로 추가

        if (!Files.exists(saveImagesPath)) { // 해당 폴더 없으면
            Files.createDirectories(saveImagesPath); // 생성
        }

        for (MultipartFile file : imageFiles) {
            String fileName = UUID.randomUUID() + "" + file.getOriginalFilename(); // 파일 이름 : 고유식별번호 + 원래 이름

            Path filePath = saveImagesPath.resolve(fileName); // 파일 경로 : 해당 폴더 + 파일 이름

            file.transferTo(filePath.toFile()); // 파일 경로 => 파일 변환 후 해당 경로에 파일 저장

            IssueImage issueImage = IssueImage.builder()
                    .imageUrl(filePath.toString())
                    .build();
            issueImage.setIssue(issue);
            issueImageRepository.save(issueImage);
        }
    }

    private void saveIssueModifications(Issue issue, State from, State to) {
        IssueModification issueModification = IssueModification.builder()
                .from(from)
                .to(to)
                .modifier(null)
                .build();
        issueModification.setIssue(issue);
        issueModificationRepository.save(issueModification);
    }
}