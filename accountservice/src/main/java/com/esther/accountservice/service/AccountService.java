package com.esther.accountservice.service;

import com.esther.accountservice.dao.AccountRepository;
import com.esther.accountservice.entity.Account;
import com.esther.accountservice.payload.AccountDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account createAccount(AccountDto accountDto) {
        if(accountRepository.existsByUsername(accountDto.getUsername())) {
            throw new RuntimeException("Username already in use.");
        }
        if(accountRepository.existsByEmail(accountDto.getEmail())) {
            throw new RuntimeException("Email already in use.");
        }
        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        return accountRepository.save(account);
    }

    private Account setAccount(AccountDto accountDto, Account account) {
        account.setEmail(accountDto.getEmail());
        account.setPassword(accountDto.getPassword()); // Consider using a password encoder
        account.setShippingAddress(accountDto.getShippingAddress());
        account.setBillingAddress(accountDto.getBillingAddress());
        account.setPaymentMethod(accountDto.getPaymentMethod());
        return accountRepository.save(account);
    }

//    @Transactional
    public Account updateAccount(Long id, AccountDto accountDto) {
        logger.info("Updating account with ID: {}", id);
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!existingAccount.getUsername().equals(accountDto.getUsername()) &&
                accountRepository.existsByUsername(accountDto.getUsername())) {
            throw new RuntimeException("Username already in use.");
        }
        if (!existingAccount.getEmail().equals(accountDto.getEmail()) &&
                accountRepository.existsByEmail(accountDto.getEmail())) {
            throw new RuntimeException("Email already in use.");
        }
        logger.info("Current email: {}", existingAccount.getEmail());
        existingAccount.setEmail(accountDto.getEmail());
        existingAccount.setPassword(accountDto.getPassword());
        existingAccount.setShippingAddress(accountDto.getShippingAddress());
        existingAccount.setBillingAddress(accountDto.getBillingAddress());
        existingAccount.setPaymentMethod(accountDto.getPaymentMethod());
        logger.info("New email: {}", existingAccount.getEmail());
        return accountRepository.save(existingAccount);
    }

    public Account updateAccountPartial(Long id, Map<String, Object> updates) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        updates.forEach((key, value) -> {
            switch (key) {
                case "username":
                    if (!account.getUsername().equals(value) &&
                            accountRepository.existsByUsername((String) value)) {
                        throw new RuntimeException("Username already in use.");
                    }
                    account.setUsername((String) value);
                    break;
                case "email":
                    if (!account.getEmail().equals(value) &&
                            accountRepository.existsByEmail((String) value)) {

                    logger.info("Email already in use.");
                    throw new RuntimeException("Email already in use.");
                    }
                    account.setEmail((String) value);
                    break;
                case "password":
                    account.setPassword((String) value); // Consider hashing the password
                    break;
                case "shippingAddress":
                    account.setShippingAddress((String) value);
                    break;
                case "billingAddress":
                    account.setBillingAddress((String) value);
                    break;
                case "paymentMethod":
                    account.setPaymentMethod((String) value);
                    break;
            }
        });
        return accountRepository.save(account);
    }


    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}

