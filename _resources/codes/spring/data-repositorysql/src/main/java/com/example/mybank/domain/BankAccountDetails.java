package com.example.mybank.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bank_account_details")
public class BankAccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    int accountId;

    @Column(name = "balance_amount")
    float balanceAmount;

    @Column(name = "last_transaction_ts")
    Date lastTransactionTs;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public float getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(float balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Date getLastTransactionTs() {
        return lastTransactionTs;
    }

    public void setLastTransactionTs(Date lastTransactionTs) {
        this.lastTransactionTs = lastTransactionTs;
    }
}
