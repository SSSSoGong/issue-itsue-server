package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Comment;
import com.ssssogong.issuemanager.domain.CommentImage;
import com.ssssogong.issuemanager.repository.CommentImageRepository;
import com.ssssogong.issuemanager.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
public class ImageService {

    private final CommentImageRepository commentImageRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void saveImages(Long commentId, List<MultipartFile> imageFiles) throws IOException {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(IllegalArgumentException::new);


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
                    .imageUrl(filePath.toString())
                    .build();

            commentImage.setComment(comment); // 연관관계 주입
            commentImageRepository.save(commentImage);
        }
    }

    @Transactional
    public void deleteImageFiles(Long commentId) {
        List<CommentImage> commentImages = commentImageRepository.findByCommentId(commentId);

        commentImageRepository.deleteByCommentId(commentId); // 기존 comment에 연결된 image 객체 삭제

        for (CommentImage commentImage : commentImages) {
            String imageUrl = commentImage.getImageUrl();
            System.out.println(imageUrl);
            File deleteFile = new File(imageUrl);
            deleteFile.delete();
        }
    }
}
