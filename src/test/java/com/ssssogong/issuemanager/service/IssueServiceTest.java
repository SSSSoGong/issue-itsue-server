//package com.ssssogong.issuemanager.service;
//
//import com.ssssogong.issuemanager.domain.Issue;
//import com.ssssogong.issuemanager.domain.Project;
//import com.ssssogong.issuemanager.domain.account.User;
//import com.ssssogong.issuemanager.dto.*;
//import com.ssssogong.issuemanager.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import static org.mockito.ArgumentMatchers.any;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class IssueServiceTest {
//
//    @Mock
//    private IssueRepository issueRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @Mock
//    private IssueImageRepository issueImageRepository;
//
//    @Mock
//    private IssueModificationRepository issueModificationRepository;
//
//    @Mock
//    private SecurityContext securityContext;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private IssueService issueService;
//
//    @BeforeEach
//    void setUp() {
//        // 보안 컨텍스트 및 모의 객체 생성
//        SecurityContext securityContext = mock(SecurityContext.class);
//        Authentication authentication = mock(Authentication.class);
//
//        // 사용자 이름 설정
//        when(authentication.getName()).thenReturn("testUser");
//
//        // 보안 컨텍스트에서 인증 객체 반환 설정
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//
//        // 보안 컨텍스트 설정
//        SecurityContextHolder.setContext(securityContext);
//    }
//
//    @DisplayName("이슈 생성")
//    @Test
//    void testSaveIssue() throws IOException {
//        // given
//        User reporter = mock(User.class);
//        Project project = mock(Project.class);
//        Issue issue = mock(Issue.class);
//        IssueImageRequestDto imageRequestDto = IssueImageRequestDto.builder().imageFiles(List.of(mock(MultipartFile.class))).build();
//        IssueSaveRequestDto saveRequestDto = IssueSaveRequestDto.builder().projectId(1L).title("title").description("description").build();
//
//        when(userRepository.findByAccountId("spdlqj4818@cau.ac.kr")).thenReturn(Optional.of(reporter));
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//        when(issueRepository.save(any(Issue.class))).thenReturn(issue);
//        when(issue.getId()).thenReturn(1L);
//
//        // when
//        Long issueId = issueService.save(imageRequestDto, saveRequestDto);
//
//        // then
//        assertEquals(1L, issueId);
//    }
//
//    @DisplayName("이슈 확인")
//    @Test
//    void testShowIssue() {
//        // given
//        Issue issue = mock(Issue.class);
//        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
//
//        // when
//        IssueResponseDto responseDto = issueService.show(1L);
//
//        // then
//        assertNotNull(responseDto);
//    }
//
//    @DisplayName("이슈 수정")
//    @Test
//    void testUpdateIssue() throws IOException {
//        // given
//        Issue issue = mock(Issue.class);
//        IssueImageRequestDto imageRequestDto = IssueImageRequestDto.builder().imageFiles(List.of(mock(MultipartFile.class))).build();
//        IssueUpdateRequestDto updateRequestDto = IssueUpdateRequestDto.builder().title("title").description("description").priority("MAJOR").build();
//
//        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
//        when(issue.getId()).thenReturn(1L);
//
//        // when
//        Long issueId = issueService.update(1L, imageRequestDto, updateRequestDto);
//
//        // then
//        assertEquals(1L, issueId);
//    }
//
//    @DisplayName("이슈 삭제")
//    @Test
//    void testDeleteIssue() {
//        // given
//        when(issueRepository.existsById(1L)).thenReturn(true);
//
//        // when
//        issueService.delete(1L);
//
//        // then
//        assertDoesNotThrow(() -> issueService.delete(1L));
//    }
//
//    @DisplayName("이슈 상태 변경")
//    @Test
//    void testStateUpdate() {
//        // given
//        Issue issue = mock(Issue.class);
//        IssueStateUpdateRequestDto stateUpdateRequestDto = IssueStateUpdateRequestDto.builder().state("ASSIGNED").assignee("assignee").build();
//
//        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
//        when(userRepository.findByUsername("assignee")).thenReturn(Optional.of(mock(User.class)));
//
//        // when
//        issueService.stateUpdate(1L, stateUpdateRequestDto);
//
//        // then
//        assertDoesNotThrow(() -> issueService.stateUpdate(1L, stateUpdateRequestDto));
//    }
//
//    @DisplayName("프로젝트에 속한 이슈 검색")
//    @Test
//    void testFindIssuesInProject() {
//        // given
//        Project project = mock(Project.class);
//        List<Issue> issues = List.of(mock(Issue.class), mock(Issue.class));
//
//        when(project.getId()).thenReturn(1L);
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//        when(issueRepository.findByProjectId(1L)).thenReturn(issues);
//
//        // when
//        List<IssueProjectResponseDto> responseDtos = issueService.findIssuesInProject(1L, null, null, null);
//
//        // then
//        assertNotNull(responseDtos);
//        assertEquals(2, responseDtos.size());
//    }
//}
