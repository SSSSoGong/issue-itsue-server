package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueImage;
import com.ssssogong.issuemanager.domain.enumeration.Category;
import com.ssssogong.issuemanager.domain.enumeration.Priority;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.repository.IssueImageRepository;
import com.ssssogong.issuemanager.repository.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
public class IssueImageServiceTest {

    @Autowired
    private IssueImageService issueImageService;

    @Autowired
    private IssueImageRepository issueImageRepository;

    @Autowired
    private IssueRepository issueRepository;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // 테스트용 디렉토리 생성
        Path saveImagesPath = tempDir.resolve("saveimages");
        Files.createDirectories(saveImagesPath);

        final Issue issue = Issue.builder()
                .id(1L)
                .title("엄청난 이슈")
                .description("엄청난 이슈입니다")
                .priority(Priority.MAJOR)
                .state(State.ASSIGNED)
                .category(Category.REFACTORING)
                .build();
        issueRepository.save(issue);
    }

    @Test
    void 이미지_저장() throws IOException {
        // given
        final Long issueId = 1L;
        final MockMultipartFile file1 = new MockMultipartFile("files", "test1.jpg", "image/jpeg", "test image1".getBytes());
        final MockMultipartFile file2 = new MockMultipartFile("files", "test2.jpg", "image/jpeg", "test image2".getBytes());

        // when
        issueImageService.save(issueId, List.of(file1, file2));

        // then
        List<IssueImage> savedImages = issueImageRepository.findByIssueId(issueId);
        assertThat(savedImages).isNotEmpty();
        assertThat(savedImages).hasSize(2);
    }

    @Test
    void 이미지_삭제() throws IOException {
        // given
        final Long issueId = 1L;
        final MockMultipartFile file = new MockMultipartFile("files", "test.jpg", "image/jpeg", "test image".getBytes());
        issueImageService.save(issueId, List.of(file));

        // when
        issueImageService.delete(issueId);

        // then
        List<IssueImage> deletedImages = issueImageRepository.findByIssueId(issueId);
        assertThat(deletedImages).isEmpty();
    }
}

