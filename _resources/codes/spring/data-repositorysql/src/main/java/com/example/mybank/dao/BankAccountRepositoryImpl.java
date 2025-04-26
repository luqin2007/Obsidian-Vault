package com.example.mybank.dao;

import com.example.mybank.domain.BankAccountDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class BankAccountRepositoryImpl implements BankAccountRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void subtractFromAccount(int bankAccountId, float amount) {
        BankAccountDetails bankAccount = em.find(BankAccountDetails.class, bankAccountId);
        if (bankAccount.getBalanceAmount() < amount)
            throw new IllegalArgumentException("Not enough money on account");
        bankAccount.setBalanceAmount(bankAccount.getBalanceAmount() - amount);
        em.merge(bankAccount);
    }
}
