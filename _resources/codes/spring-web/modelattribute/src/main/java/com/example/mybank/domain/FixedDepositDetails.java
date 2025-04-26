package com.example.mybank.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class FixedDepositDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private int bankAccountId;
    private Date creationDate;
    private Date maturityDate;
    private long depositAmount;
    private int tenure;
    private String active;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public long getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(long depositAmount) {
        this.depositAmount = depositAmount;
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
                "id=" + id +
                ", bankAccountId=" + bankAccountId +
                ", creationDate=" + creationDate +
                ", maturityDate=" + maturityDate +
                ", depositAmount=" + depositAmount +
                ", tenure=" + tenure +
                ", active='" + active + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
