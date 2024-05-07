package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Member_Project;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MPRepository {

    private final EntityManager em;

    public void save(Member_Project memberProject) {
        em.persist(memberProject);
    }

    public Member_Project findOne(Long roleId) {
        return em.find(Member_Project.class, roleId);
    }

    public List<Member_Project> findAll() {
        return em.createQuery("select r from Role r", Member_Project.class)
                .getResultList();
    }

}
