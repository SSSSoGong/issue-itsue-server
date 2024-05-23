package com.ssssogong.issuemanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ssssogong.issuemanager.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "/truncate.sql")
class RoleInitializerTest {

    @Autowired
    private RoleInitializer roleInitializer;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void contextLoads() {
        assertNotNull(roleInitializer);
    }

    @Test
    void readRoleListSize() {
        assertThat(roleRepository.findAll().size()).isEqualTo(3);
    }
}
