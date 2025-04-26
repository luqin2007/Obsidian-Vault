package com.example.mybank.service;

import com.example.mybank.dao.BankAccountRepository;
import com.example.mybank.domain.BankAccountDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public void createAccount(BankAccountDetails bankAccountDetails) {
        bankAccountRepository.save(bankAccountDetails);
    }
}
