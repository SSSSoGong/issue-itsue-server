package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentImageRepository extends JpaRepository<CommentImage, Long> {
    List<CommentImage> findByCommentId(Long commentId);

    void deleteByCommentId(Long commentId);
}
