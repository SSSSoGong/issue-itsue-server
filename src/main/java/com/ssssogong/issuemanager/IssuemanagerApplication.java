package com.ssssogong.issuemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IssuemanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssuemanagerApplication.class, args);
    }

}

/*
TODO: 테스트코드 리팩토링
TODO: 로컬파일 삭제
TODO: 매퍼...
 */

