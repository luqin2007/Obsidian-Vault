package com.example.mybank;

import java.util.Date;

public class FixedDepositDetails {

    private int bankAccountId;
    private Date creationDate;
    private int depositAmount;
    private int tenure;
    private boolean active;

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "FixedDepositDetails{" +
                "bankAccountId=" + bankAccountId +
                ", creationDate=" + creationDate +
                ", depositAmount=" + depositAmount +
                ", tenure=" + tenure +
                ", active=" + active +
                '}';
    }
}