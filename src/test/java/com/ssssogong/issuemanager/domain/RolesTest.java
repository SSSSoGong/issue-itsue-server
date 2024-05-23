package com.ssssogong.issuemanager.domain;

import com.ssssogong.issuemanager.domain.role.Developer;
import com.ssssogong.issuemanager.domain.role.ProjectLeader;
import com.ssssogong.issuemanager.domain.role.Tester;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RolesTest {

    @Test
    void 역할을_찾는다() {
        final Roles roles = Roles.builder()
                .roles(List.of(new Tester(), new ProjectLeader(), new Developer()))
                .build();

        assertThat(roles.findRole("Tester").getRoleName()).isEqualToIgnoringCase("tester");
        assertThat(roles.findRole("ProjectLeader").getRoleName()).isEqualToIgnoringCase("projectLeader");
        assertThat(roles.findRole("Developer").getRoleName()).isEqualToIgnoringCase("developer");
    }
}
