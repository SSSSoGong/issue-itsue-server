package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.repository.IssueRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
public class IssueServiceTest {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 이슈_생성
}
