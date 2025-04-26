package com.example.mybank.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collation = "bank_accounts")
public class BankAccountDetails {

    @Id
    String accountId;
    int balance;
    Date lastTransactionTimestamp;
    List<FixedDepositDetails> fixedDepositDetails;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Date getLastTransactionTimestamp() {
        return lastTransactionTimestamp;
    }

    public void setLastTransactionTimestamp(Date lastTransactionTimestamp) {
        this.lastTransactionTimestamp = lastTransactionTimestamp;
    }

    public List<FixedDepositDetails> getFixedDepositDetails() {
        return fixedDepositDetails;
    }

    public void setFixedDepositDetails(List<FixedDepositDetails> fixedDepositDetails) {
        this.fixedDepositDetails = fixedDepositDetails;
    }
}
