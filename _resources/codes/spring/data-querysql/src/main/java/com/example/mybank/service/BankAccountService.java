package com.example.mybank.service;

import com.example.mybank.dao.BankAccountRepository;
import com.example.mybank.domain.BankAccountDetails;
import com.example.mybank.domain.QBankAccountDetails;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public BankAccountDetails createAccount(BankAccountDetails bankAccountDetails) {
        return bankAccountRepository.save(bankAccountDetails);
    }

    public void subtractAmount(int bankAccountId, float amount) {
        Predicate predicate = QBankAccountDetails.bankAccountDetails.accountId.eq(bankAccountId);
        BankAccountDetails accountDetails = bankAccountRepository.findOne(predicate).orElseThrow();
        accountDetails.setBalanceAmount(accountDetails.getBalanceAmount() - amount);
        bankAccountRepository.save(accountDetails);
    }
}
