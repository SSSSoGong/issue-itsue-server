package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.account.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void setter가_작동하는지_확인한다(){
        // given
        User user = User.builder()
                .username("test1")
                .build();

        // when
        user.setUsername("test2");

        // then
        Assertions.assertThat(user.getUsername()).isEqualTo("test2");
    }
}
