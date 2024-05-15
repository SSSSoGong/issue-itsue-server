package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
