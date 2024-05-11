package com.ssssogong.issuemanager.builder;

import com.ssssogong.issuemanager.domain.account.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SupperBuilderTest {

    @Test
    public void superBuilderTest() {
        User user = User.builder().accountId("1").username("Hyun").password("1").createdAt(LocalDateTime.now()).build();
        System.out.println(user.getAccountId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getCreatedAt());
    }
}
