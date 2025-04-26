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
    @Column(name = "account_id")
    private int accountId;
    @Column(name = "fd_creation_date")
    private Date fdCreationDate;
    @Column(name = "fd_maturity_date")
    private Date fdMaturityDate;
    @Column(name = "amount")
    private long amount;
    @Column(name = "tenure")
    private int tenure;
    @Column(name = "active")
    private String active;
    @Column(name = "email")
    private String email;

    public int getFixedDepositId() {
        return fixedDepositId;
    }

    public void setFixedDepositId(int id) {
        this.fixedDepositId = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int bankAccountId) {
        this.accountId = bankAccountId;
    }

    public Date getFdCreationDate() {
        return fdCreationDate;
    }

    public void setFdCreationDate(Date creationDate) {
        this.fdCreationDate = creationDate;
    }

    public Date getFdMaturityDate() {
        return fdMaturityDate;
    }

    public void setFdMaturityDate(Date maturityDate) {
        this.fdMaturityDate = maturityDate;
    }

    public long getAAmount() {
        return amount;
    }

    public void setAAmount(long depositAmount) {
        this.amount = depositAmount;
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
}
