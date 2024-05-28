package com.ssssogong.issuemanager.security;

import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.domain.role.Privilege;
import com.ssssogong.issuemanager.dto.IssueStateUpdateRequestDto;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * 프로젝트 권한 체크 모듈 <br> @PreAuthorize로 메소드 단위 권한 체크에 활용
 */
@Component("ProjectPrivilegeEvaluator")
@RequiredArgsConstructor
public class ProjectPrivilegeEvaluator {
    private final ProjectRepository projectRepository;
    private final IssueRepository issueRepository;
    private final UserProjectRepository userProjectRepository;
    // 권한 확인을 위해선 몇 번째 project에 접근해야하는지 알아야하기 때문에 메소드 레벨 권한 체크가 필요하다
    /**
     * 권한 체크 방법
     * 메소드 시그니처 위에 @PreAuthorize('...') 혹은 @PostAuthorize('...') 어노테이션 붙여서 본 클래스 호출
     * (@PreAuthorize: 메소드 실행 전에 권한 체크 / @PostAuthorize: 메소드 실행 후 return 직전에 권한 체크.  실패 시 403 FORBIDDEN)
     *
     *ex) @PreAuthorize('@ProjectPrivilegeEvaluator.hasPrivilege(#projectId, @Privilege.ISSUE_CREATABLE')
     *    public void method1(Long projectId){ ... }
     *    -> 현재 유저가 projectId에 ISSUE_CREATABLE 권한이 있는지 확인한다
     *
     *    주의점 : projectId는 메소드의 매개변수에 있어야 함
     *      (어노테이션 특성 상 매개변수가 아니면 가져올 수 없음)
     */

    /**
     * 콘솔에 권한 출력 (디버깅용)
     */
    private void printPrivileges(Authentication authentication) {
        System.out.print("ProjectPermissionEvaluator: account id '" + authentication.getName() + "' with role [ ");
        authentication.getAuthorities().forEach(auth -> System.out.print(auth.getAuthority() + " "));
        System.out.println("]");
    }

    /**
     * 유저가 해당 project에 permission이 있는지 확인<br>
     * ex) @PreAuthorize('@ProjectPrivilegeEvaluator.hasPrivilege(#projectId, @Privilege.ISSUE_REPORTABLE')
     */
    public boolean hasPrivilege(Long projectId, Privilege privilege) {
        // 권한 출력
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        printPrivileges(authentication);

        // 권한의 형태 : DEVELOPER_TO_PROJECT_ID 혹은 ISSUE_FIXABLE_TO_PROJECT_ID 등
        // permission이 포함되고 projectName으로 끝나는 권한이 있는지 찾는다
        for (GrantedAuthority grantedAuth : authentication.getAuthorities()) {
            if (grantedAuth.getAuthority().endsWith(String.valueOf(projectId)) && grantedAuth.getAuthority().contains(privilege.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 유저가 해당 프로젝트의 생성자인지 확인
     */
    public boolean isAdmin(Long projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        printPrivileges(authentication);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("project " + projectId + " not found"));
        return Objects.equals(project.getAdmin().getAccountId(), authentication.getName());
    }

    /**
     * 유저가 해당 이슈에 상태 변경 권한이 있는지 확인
     */
    public boolean canChangeIssueState(Long issueId, IssueStateUpdateRequestDto updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        printPrivileges(authentication);
        // issueId로부터 projectId 조회
        Long projectId = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("issue " + issueId + " not found"))
                .getProject().getId();

        // issueId로부터 state 조회
        String changeFrom = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("issue " + issueId + "not found"))
                .getState().toString();
        String changeTo = updateRequest.getState();

        // state 변경은 2단계 이상 건너 뛸 수 없다.
        // 변경할 state에 따라 권한 판단
        return switch (changeTo) {
            case "NEW" -> hasPrivilege(projectId, Privilege.ISSUE_REPORTABLE);
            case "ASSIGNED" -> changeFrom.equals("NEW") && hasPrivilege(projectId, Privilege.ISSUE_ASSIGNABLE);
            case "FIXED" -> changeFrom.equals("ASSIGNED") && hasPrivilege(projectId, Privilege.ISSUE_FIXABLE) && isAssignee(issueId);
            case "RESOLVED" -> changeFrom.equals("FIXED") && hasPrivilege(projectId, Privilege.ISSUE_RESOLVABLE) && isReporter(issueId);
            case "CLOSED" -> changeFrom.equals("RESOLVED") && hasPrivilege(projectId, Privilege.ISSUE_CLOSABLE);
            case "REOPENED" -> changeFrom.equals("CLOSED") && hasPrivilege(projectId, Privilege.ISSUE_REOPENABLE) && isReporter(issueId);
            default -> throw new IllegalArgumentException("input state " + changeTo + " is not valid");
        };
    }


    /**
     * 유저와 이슈 작성자가 일치하는지 확인
     */
    public boolean isReporter(Long issueId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        printPrivileges(authentication);
        // issueId의 reporter와 유저 비교
        User owner = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("issue " + issueId + " not found"))
                .getReporter();
        Objects.requireNonNull(owner, "issue " + issueId + " does not have reporter");
        String ownerId = owner.getAccountId();
        return ownerId.equals(authentication.getName());
    }

    /**
     * 유저와 이슈 assignee가 일치하는지 확인
     */
    public boolean isAssignee(Long issueId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        printPrivileges(authentication);
        // issueId의 reporter와 유저 비교
        User assignee = issueRepository.findById(issueId)
                .orElseThrow(() -> new NotFoundException("issue " + issueId + " not found"))
                .getAssignee();
        Objects.requireNonNull(assignee, "issue " + issueId + " does not have assignee");
        String assigneeId = assignee.getAccountId();
        return assigneeId.equals(authentication.getName());
    }
}
