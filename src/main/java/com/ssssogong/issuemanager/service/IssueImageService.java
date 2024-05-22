package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueImage;
import com.ssssogong.issuemanager.repository.IssueImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueImageService {

    private final IssueImageRepository issueImageRepository;

    @Transactional
    public void save(Issue issue, List<MultipartFile> imageFiles) throws IOException {
        Path currentPath = Paths.get("").toAbsolutePath();  // 현재 작업 절대경로
        Path saveImagesPath = currentPath.resolve("saveimages"); // 현재 경로에 save_images 경로 추가

        if (!Files.exists(saveImagesPath)) { // 해당 폴더 없으면
            Files.createDirectories(saveImagesPath); // 생성
        }

        for (MultipartFile file : imageFiles) {
            String fileName = UUID.randomUUID() + "" + file.getOriginalFilename(); // 파일 이름 : 고유식별번호 + 원래 이름

            Path filePath = saveImagesPath.resolve(fileName); // 파일 경로 : 해당 폴더 + 파일 이름

            file.transferTo(filePath.toFile()); // 파일 경로 => 파일 변환 후 해당 경로에 파일 저장

            IssueImage issueImage = IssueImage.builder()
                    .imageUrl(filePath.toString())
                    .build();
            issueImage.setIssue(issue);
            issueImageRepository.save(issueImage);
        }
    }

    @Transactional
    public void deleteInLocal(Issue issue) {
        List<IssueImage> issueImages = issue.getIssueImages();
        for (IssueImage issueImage : issueImages) {
            File file = new File(issueImage.getImageUrl());
            if (file.exists()) {
                if (file.delete())
                    System.out.println("Success File delete");
                else
                    System.out.println("Fail File delete");
            } else
                System.out.println("File not exists");
        }
    }
}
