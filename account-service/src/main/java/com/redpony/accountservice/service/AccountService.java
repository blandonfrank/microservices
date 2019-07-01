package com.redpony.accountservice.service;

import com.redpony.accountservice.client.AuthServiceClient;
import com.redpony.accountservice.exceptions.AccountAlreadyExistsException;
import com.redpony.accountservice.model.Account;
import com.redpony.accountservice.model.User;
import com.redpony.accountservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthServiceClient authServiceClient;

    public Account create(User user) throws AccountAlreadyExistsException {

        Account existing = accountRepository.findByUsername(user.getUsername());
        if(!Objects.isNull(existing))
            throw new AccountAlreadyExistsException("Account already exists with username: " + user.getUsername());

        authServiceClient.createUser(user);

        Account account = new Account();
        account.setUsername(user.getUsername());
        account.setLastSeen(new Date());

        accountRepository.save(account);

        log.info("new account has been created: {}", account.getUsername());

        return account;
    }
}
