package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.Role;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleRepository {

    private final EntityManager em;

    public void save(Role role) {
        em.persist(role);
    }

    public Role findOne(Long roleId) {
        return em.find(Role.class, roleId);
    }

    public List<Role> findAll() {
        return em.createQuery("select r from Role r", Role.class)
                .getResultList();
    }
}
