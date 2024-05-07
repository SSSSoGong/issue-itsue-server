package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Member;
import com.ssssogong.issuemanager.domain.Project;
import com.ssssogong.issuemanager.domain.Member_Project;
import com.ssssogong.issuemanager.repository.MPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MPService {

    private final MPRepository MPRepository;

    public void saveRole(String roleName, Member member, Project project) {
        Member_Project memberProject = new Member_Project();
        memberProject.setRole(roleName);
        memberProject.setMember(member);
        memberProject.setProject(project);
        MPRepository.save(memberProject);
    }

    public Member_Project findOne(Long roleId) {
        return MPRepository.findOne(roleId);
    }

    public List<Member_Project> findAll() {
        return MPRepository.findAll();
    }
}
