package com.example.mybank.dao;

public interface BankAccountRepositoryCustom {

    void subtractFromAccount(int bankAccountId, float count);
}
