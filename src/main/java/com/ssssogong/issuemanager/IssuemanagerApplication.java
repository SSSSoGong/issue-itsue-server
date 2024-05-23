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
