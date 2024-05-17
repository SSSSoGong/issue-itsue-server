package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Getter
public class CommentService {
    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    String uploadDir;

    @Transactional
    public Long createComment(Long issueId, CommentRequestDto commentRequestDto) throws IOException {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IllegalArgumentException::new);
        User writer = userRepository.findByAccountId(commentRequestDto.getWriterAccountId())
                .orElseThrow(IllegalArgumentException::new);

        List<String> imageUrls = saveImages(commentRequestDto.getImageFiles());

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .imageUrls(imageUrls)
                .issue(issue)
                .writer(writer)
                .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment).getId();
    }

    @Transactional
    public CommentResponseDto getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new CommentResponseDto(comment);
    }

    public Long updateComment(Long id, CommentRequestDto commentRequestDto) throws IOException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        List<String> imageUrls = saveImages(commentRequestDto.getImageFiles());

        comment.update(commentRequestDto.getContent(), imageUrls);

        return new CommentResponseDto(comment).getId();

    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        commentRepository.delete(comment);
    }

    private List<String> saveImages(List<MultipartFile> imageFiles) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));
            imageUrls.add(filePath);
        }
        return imageUrls;
    }

}
