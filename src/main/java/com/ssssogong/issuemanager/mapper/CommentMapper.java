package com.ssssogong.issuemanager.mapper;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentIdResponseDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;

import java.util.List;
import java.util.stream.Collectors;


public class CommentMapper {

    public static CommentResponseDto toCommentResponseDto(final Comment comment) {
        // 코멘트 하나 반환
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writerId(comment.getWriter().getAccountId())
                .writerName(comment.getWriter().getUsername())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .imageUrls(comment.getCommentImages().stream()
                        .map(CommentImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();

    }

    public static List<CommentResponseDto> toCommentResponseDto(final List<Comment> comments) {
        // 이슈에 속한 코멘트 전체 반환
        return comments.stream()
                .map(each -> CommentResponseDto.builder()
                        .id(each.getId())
                        .content(each.getContent())
                        .writerId(each.getWriter().getAccountId())
                        .writerName(each.getWriter().getUsername())
                        .createdAt(each.getCreatedAt())
                        .updatedAt(each.getUpdatedAt())
                        .imageUrls(each.getCommentImages().stream()
                                .map(CommentImage::getImageUrl)
                                .collect(Collectors.toList()))
                        .build()
                ).toList();
    }

    public static CommentIdResponseDto toCommentIdResponseDto(final Long commentId) {
        // 코멘트 아이디 반환
        return CommentIdResponseDto.builder()
                .commentId(commentId)
                .build();
    }

    public static Comment toCommentRequestDto(final Issue issue, final User writer, final CommentRequestDto commentRequestDto) {
       // 코멘트 생성
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .writer(writer)
                .issue(issue)
                .build();
    }

}
