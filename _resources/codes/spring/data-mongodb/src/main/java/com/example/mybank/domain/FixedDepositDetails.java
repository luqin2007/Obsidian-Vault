package com.example.mybank.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class FixedDepositDetails {

    @Id
    private ObjectId fixedDepositId;
    private int tenure;
    private int fdAmount;

    public FixedDepositDetails() {
        fixedDepositId = new ObjectId();
    }

    public ObjectId getFixedDepositId() {
        return fixedDepositId;
    }

    public void setFixedDepositId(ObjectId fixedDepositId) {
        this.fixedDepositId = fixedDepositId;
    }

    public int getFdAmount() {
        return fdAmount;
    }

    public void setFdAmount(int fdAmount) {
        this.fdAmount = fdAmount;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }
}
