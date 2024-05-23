package com.ssssogong.issuemanager.security;

import com.ssssogong.issuemanager.domain.role.Privilege;
import com.ssssogong.issuemanager.repository.ProjectRepository;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * 프로젝트 권한 체크 모듈 <br> @PreAuthorize로 메소드 단위 권한 체크에 활용
 */
@Component("ProjectPrivilegeEvaluator")
@RequiredArgsConstructor
public class ProjectPrivilegeEvaluator {
    private final ProjectRepository projectRepository;
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
     * 유저가 해당 project에 permission이 있는지 확인<br>
     * ex) @PreAuthorize('@ProjectPrivilegeEvaluator.hasPrivilege(#projectId, @Privilege.ISSUE_REPORTABLE')
     */
    public boolean hasPrivilege(Long projectId, Privilege privilege) {
        // 권한 출력
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.print("ProjectPermissionEvaluator: " + authentication.getName() + " with role [ ");
        authentication.getAuthorities().forEach(auth -> System.out.print(auth.getAuthority() + " "));
        System.out.println("]");

        // 권한의 형태 : DEVELOPER_TO_PROJECT_ID 혹은 ISSUE_FIXABLE_TO_PROJECT_ID 등
        // permission이 포함되고 projectName으로 끝나는 권한이 있는지 찾는다
        for (GrantedAuthority grantedAuth : authentication.getAuthorities()) {
            if (grantedAuth.getAuthority().endsWith(String.valueOf(projectId)) && grantedAuth.getAuthority().contains(privilege.toString())) {
                return true;
            }
        }
        return false;
    }
}
