package com.example.mybank.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "fixed_deposit_details")
public class FixedDepositDetails {

    @Id
    @Column(name = "fixed_deposit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fixedDepositId;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccountDetails accountId;
    @Column(name = "fd_creation_date")
    private Date fdCreationDate;
    @Column(name = "fd_maturity_date")
    private Date fdMaturityDate;
    @Column(name = "amount")
    private long amount;
    @Column(name = "tenure")
    private int tenure;
    @Column(name = "active", insertable = false)
    private String active;
    @Column(name = "email")
    private String email;

    public int getFixedDepositId() {
        return fixedDepositId;
    }

    public void setFixedDepositId(int fixedDepositId) {
        this.fixedDepositId = fixedDepositId;
    }

    public BankAccountDetails getAccountId() {
        return accountId;
    }

    public void setAccountId(BankAccountDetails accountId) {
        this.accountId = accountId;
    }

    public Date getFdCreationDate() {
        return fdCreationDate;
    }

    public void setFdCreationDate(Date fdCreationDate) {
        this.fdCreationDate = fdCreationDate;
    }

    public Date getFdMaturityDate() {
        return fdMaturityDate;
    }

    public void setFdMaturityDate(Date fdMaturityDate) {
        this.fdMaturityDate = fdMaturityDate;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "FixedDepositDetails{" +
                "fixedDepositId=" + fixedDepositId +
                ", accountId=" + accountId +
                ", fdCreationDate=" + fdCreationDate +
                ", fdMaturityDate=" + fdMaturityDate +
                ", amount=" + amount +
                ", tenure=" + tenure +
                ", active='" + active + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
