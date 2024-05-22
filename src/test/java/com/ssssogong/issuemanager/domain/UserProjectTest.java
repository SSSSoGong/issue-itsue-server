package com.ssssogong.issuemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class UserProjectTest {

    @Test
    void 유저의_프로젝트_접근_시간을_업데이트한다() {
        // given
        final UserProject userProject = UserProject.builder().build();
        final LocalDateTime originalAccessTime = userProject.getAccessTime();

        // when
        userProject.updateAccessTime();
        final LocalDateTime updatedAccessTime = userProject.getAccessTime();

        // then
        assertThat(originalAccessTime.isBefore(updatedAccessTime)).isTrue();
    }
}
