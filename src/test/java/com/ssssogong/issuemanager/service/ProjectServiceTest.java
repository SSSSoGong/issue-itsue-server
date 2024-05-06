package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Member;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    ProjectService projectService;
    @Autowired
    AccountService accountService;
    @Autowired
    RoleService roleService;

    @Test
    public void 프로젝트_추가() throws Exception {
        Project project = new Project();
        project.setProjectName("SE");
        project.setSubject("OOAD");

        projectService.saveProject(project);
        assertEquals(project, projectService.findOne(project.getId()));
    }

    @Test
    public void 프로젝트_인원추가() throws Exception {
        Project project = new Project();
        project.setProjectName("SE");
        project.setSubject("OOAD");
        projectService.saveProject(project);

        Member member = new Member();
        member.setName("Hyun");
        accountService.loginAccount(member);

        roleService.saveRole("Tester", member, project);
    }
}
