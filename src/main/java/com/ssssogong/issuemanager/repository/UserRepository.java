package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.account.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccountId(String accountId);
}
