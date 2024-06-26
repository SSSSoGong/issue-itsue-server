package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueImage;
import com.ssssogong.issuemanager.exception.NotFoundException;
import com.ssssogong.issuemanager.repository.IssueImageRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
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

// TODO 이미지 return 할 때 url로 줘도 되나..
@Service
@RequiredArgsConstructor
public class IssueImageService {

    private final IssueRepository issueRepository;

    private final IssueImageRepository issueImageRepository;

    @Transactional
    public void save(Long issueId, List<MultipartFile> imageFiles) throws IOException {
        if (imageFiles != null && !imageFiles.isEmpty()) {
            Path currentPath = Paths.get("").toAbsolutePath();  // 현재 작업 절대경로
            Path saveImagesPath = currentPath.resolve("saveimages"); // 현재 경로에 save_images 경로 추가

            if (!Files.exists(saveImagesPath)) { // 해당 폴더 없으면
                Files.createDirectories(saveImagesPath); // 생성
            }
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 파일 이름 : 고유식별번호 + 원래 이름

                    Path filePath = saveImagesPath.resolve(fileName); // 파일 경로 : 해당 폴더 + 파일 이름

                    file.transferTo(filePath.toFile()); // 파일 경로 => 파일 변환 후 해당 경로에 파일 저장

                    Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("해당 issue가 없습니다"));
                    IssueImage issueImage = IssueImage.builder()
                            .imageUrl("/saveimages/" + fileName) // 실제 저장 주소가 아닌 프론트에게 넘길 이미지 주소!
                            .build();
                    issueImage.setIssue(issue);
                    issueImageRepository.save(issueImage);
                }
            }
        }
    }

    @Transactional
    public void delete(Long issueId) {
        List<IssueImage> issueImages = issueImageRepository.findByIssueId(issueId);
        for (IssueImage issueImage : issueImages) {
            Path currentPath = Paths.get("").toAbsolutePath();  // 현재 작업 절대경로
            String imageUrl = currentPath + issueImage.getImageUrl(); //현재 작업 절대 경로에 db에 저장되있던 url 추가
            File deleteFile = new File(imageUrl);
            deleteFile.delete();
        }
        issueImageRepository.deleteByIssueId(issueId);
    }
}
