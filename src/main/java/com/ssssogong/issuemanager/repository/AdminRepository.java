package com.ssssogong.issuemanager.repository;

import com.ssssogong.issuemanager.domain.account.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}