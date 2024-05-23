package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.account.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(String accountId);
    Optional<User> findByUsername(String username);

}
