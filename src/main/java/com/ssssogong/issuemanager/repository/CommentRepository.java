package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
