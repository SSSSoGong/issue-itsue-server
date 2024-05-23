package com.ssssogong.issuemanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserProjectSorterTest {

    @Test
    void 접근시간_최신순으로_반환한다() {
        // given
        final UserProject userProject1 = UserProject.builder().build();
        userProject1.updateAccessTime();
        final UserProject userProject2 = UserProject.builder().build();
        userProject2.updateAccessTime();
        final UserProject userProject3 = UserProject.builder().build();
        userProject3.updateAccessTime();

        final List<UserProject> userProjects = new ArrayList<>(List.of(userProject1, userProject2, userProject3));
        final List<UserProject> sorted = UserProjectSorter.sortByAccessTimeAndIsFavorite(userProjects);

        for (UserProject userProject : sorted) {
            System.out.println("userProject.getAccessTime() = " + userProject.getAccessTime());
        }

        final LocalDateTime index0AccessTime = sorted.get(0).getAccessTime();
        final LocalDateTime index1AccessTime = sorted.get(1).getAccessTime();
        final LocalDateTime index2AccessTime = sorted.get(2).getAccessTime();

        // then
        assertThat(index0AccessTime.isAfter(index1AccessTime)).isTrue();
        assertThat(index0AccessTime.isAfter(index2AccessTime)).isTrue();
        assertThat(index1AccessTime.isAfter(index2AccessTime)).isTrue();
    }

    @Test
    void 즐겨찾기한거_아직접근안한거_접근한거최신순으로_반환한다() {
        // given
        final UserProject userProject1 = UserProject.builder().build();
        userProject1.updateAccessTime();
        final UserProject userProject2 = UserProject.builder().build();
        userProject2.updateIsFavorite(true);
        userProject2.updateAccessTime();
        final UserProject userProject3 = UserProject.builder().build();
        final UserProject userProject4 = UserProject.builder().build();
        userProject4.updateAccessTime();

        final List<UserProject> userProjects = new ArrayList<>(List.of(userProject1, userProject2, userProject3, userProject4));
        final List<UserProject> sorted = UserProjectSorter.sortByAccessTimeAndIsFavorite(userProjects);

        for (UserProject userProject : sorted) {
            System.out.println("userProject.getAccessTime() = " + userProject.getAccessTime());
        }

        final UserProject index0 = sorted.get(0);
        final UserProject index1 = sorted.get(1);
        final UserProject index2 = sorted.get(2);
        final UserProject index3 = sorted.get(3);

        // then
        assertThat(index0.isFavorite()).isTrue();
        assertThat(index1.getAccessTime()).isNull();
        assertThat(index2.getAccessTime().isAfter(index3.getAccessTime())).isTrue();
    }
}
