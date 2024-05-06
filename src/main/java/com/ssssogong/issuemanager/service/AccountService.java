package com.ssssogong.issuemanager.service;

import com.ssssogong.issuemanager.domain.Account;
import com.ssssogong.issuemanager.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void loginAccount(Account account) {
        accountRepository.save(account);
    }

    public Account findOne(Long accountId) {
        return accountRepository.findOne(accountId);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
