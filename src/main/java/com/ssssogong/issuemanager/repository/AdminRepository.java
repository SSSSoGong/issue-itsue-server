package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.account.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAccountId(String adminId);
}
