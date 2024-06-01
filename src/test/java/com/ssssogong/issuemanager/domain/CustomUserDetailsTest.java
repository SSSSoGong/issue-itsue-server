package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.CustomUserDetails;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.mapper.UserMapper;
import com.ssssogong.issuemanager.repository.UserProjectRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import com.ssssogong.issuemanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
public class CustomUserDetailsTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProjectRepository userProjectRepository;
    @Autowired
    private UserService userService;
    CustomUserDetails customUserDetails;
    User user;

    @Test
    public void 권한을_확인한다(){
        User user = User.builder()
                .accountId("test id")
                .password("1234")
                .build();
        userRepository.save(user);

    }



}
