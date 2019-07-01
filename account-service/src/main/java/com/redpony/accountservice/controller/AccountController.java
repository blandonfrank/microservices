package com.redpony.accountservice.controller;

import com.redpony.accountservice.model.Account;
import com.redpony.accountservice.model.User;
import com.redpony.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/")
    public Account createNewAccount(@Valid @RequestBody User user) {
        return accountService.create(user);
    }
}
