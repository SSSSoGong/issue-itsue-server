package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.dto.CommentImageRequestDto;
import com.ssssogong.issuemanager.dto.CommentRequestDto;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.CommentRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final CommentImageRepository commentImageRepository;

    private String uploadDir;
    private String writerAccountId;

    @Transactional
    public Long createComment(Long issueId, CommentRequestDto commentRequestDto, CommentImageRequestDto commentImageRequestDto) throws IOException {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(IllegalArgumentException::new);

        writerAccountId = SecurityContextHolder.getContext().getAuthentication().getName();

        User writer = userRepository.findByAccountId(writerAccountId)
                .orElseThrow(IllegalArgumentException::new);

        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .issue(issue)
                .writer(writer)
                .build();

        commentRepository.save(comment);

        if (commentImageRequestDto.getImageFiles() != null && !commentImageRequestDto.getImageFiles().isEmpty()) {
            saveImages(comment, commentImageRequestDto.getImageFiles());
        }


        return new CommentResponseDto(comment).getId();
    }

    @Transactional
    public CommentResponseDto getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public Long updateComment(Long id, String content, List<MultipartFile> images) throws IOException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        List<CommentImage> commentImages = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            commentImages = saveImages(comment, images);
        }

        comment.getCommentImages().clear();
        comment.update(content, commentImages);

        return new CommentResponseDto(comment).getId();

    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        commentRepository.delete(comment);
    }

    private List<CommentImage> saveImages(Comment comment, List<MultipartFile> imageFiles) throws IOException {

        List<CommentImage> commentImages = new ArrayList<>();

        Path currentPath = Paths.get("").toAbsolutePath();  // 현재 작업 절대경로
        Path saveImagesPath = currentPath.resolve("save_images"); // 현재 경로에 save_images 경로 추가

        if (!Files.exists(saveImagesPath)) { // 해당 폴더 없으면
            Files.createDirectories(saveImagesPath); // 생성
        }

        for (MultipartFile file : imageFiles) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 파일 이름 : 고유식별번호 + 원래 이름

            Path filePath = saveImagesPath.resolve(fileName); // 파일 경로 : 해당 폴더 + 파일 이름

            file.transferTo(filePath.toFile()); // 파일 경로 => 파일 변환 후 해당 경로에 파일 저장

            CommentImage commentImage = CommentImage.builder()
                    .comment(comment)
                    .imageUrls(filePath.toString())
                    .build();

            commentImageRepository.save(commentImage);
            commentImages.add(commentImage);
        }
        return commentImages;
    }


}


