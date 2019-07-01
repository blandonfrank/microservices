package com.redpony.accountservice.repository;

import com.redpony.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findByUsername(String username);
}
