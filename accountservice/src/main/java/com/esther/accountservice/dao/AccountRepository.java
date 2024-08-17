package com.esther.accountservice.dao;

import com.esther.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

