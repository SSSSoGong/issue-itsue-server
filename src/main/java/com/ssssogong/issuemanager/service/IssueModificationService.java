package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Issue;
import com.ssssogong.issuemanager.domain.IssueModification;
import com.ssssogong.issuemanager.domain.enumeration.State;
import com.ssssogong.issuemanager.repository.IssueModificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueModificationService {

    private final IssueModificationRepository issueModificationRepository;

    @Transactional
    public void save(Issue issue, State from, State to){
        IssueModification issueModification = IssueModification.builder()
                .from(from)
                .to(to)
                .modifier(null)
                .build();
        issueModification.setIssue(issue);
        issueModificationRepository.save(issueModification);
    }
}
