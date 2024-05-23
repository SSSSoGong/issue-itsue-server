package com.ssssogong.issuemanager.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class UserProjectTest {

    @Test
    void 유저가_프로젝트에_초대외었을때_접근시간은_비어있다() {
        // given
        final UserProject userProject = UserProject.builder().build();

        // when
        final LocalDateTime accessTime = userProject.getAccessTime();

        // then
        assertThat(accessTime).isNull();
    }

    @Test
    void 유저의_프로젝트_접근_시간을_업데이트한다() throws InterruptedException {
        // given
        final UserProject userProject = UserProject.builder().build();
        userProject.updateAccessTime();
        Thread.sleep(100);
        final LocalDateTime originalAccessTime = userProject.getAccessTime();

        // when
        userProject.updateAccessTime();
        final LocalDateTime updatedAccessTime = userProject.getAccessTime();

        // then
        assertThat(originalAccessTime.isBefore(updatedAccessTime)).isTrue();
    }

    @Test
    void 유저의_프로젝트_즐겨찾기_여부를_업데이트한다() {
        // given
        final UserProject userProject = UserProject.builder().build();
        final boolean before = userProject.isFavorite();

        // when
        userProject.updateIsFavorite(true);
        final boolean after = userProject.isFavorite();

        // then
        assertThat(before).isFalse();
        assertThat(after).isTrue();
    }
}
