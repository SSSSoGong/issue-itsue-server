package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.dto.CommentResponseDto;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class CommentImageService {

    private final CommentImageRepository commentImageRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void createImages(Long commentId, List<MultipartFile> imageFiles) throws IOException {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);

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
        }
    }
}
