package com.ssssogong.issuemanager.security;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("ProjectPrivilegeEvaluator")
@RequiredArgsConstructor
public class CommentPrivilegeEvaluator {
    private final CommentRepository commentRepository;

    /**콘솔에 권한 출력 (디버깅용)*/
    private void printPrivileges(Authentication authentication){
        System.out.print("CommentPrivilegeEvaluator: " + authentication.getName() + " with role [ ");
        authentication.getAuthorities().forEach(auth -> System.out.print(auth.getAuthority() + " "));
        System.out.println("]");
    }

    /**유저가 해당 댓글의 작성자인지 확인한다*/
    public boolean isOwner(Long commentId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        printPrivileges(authentication);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new NotFoundException("Comment " + commentId + " not found"));
        return Objects.equals(comment.getWriter().getAccountId(), authentication.getName());
    }
}
