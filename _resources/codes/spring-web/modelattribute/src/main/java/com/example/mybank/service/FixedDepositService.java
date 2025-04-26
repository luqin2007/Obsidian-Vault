package com.example.mybank.service;

import com.example.mybank.domain.FixedDepositDetails;

import java.util.List;

public interface FixedDepositService {

    List<FixedDepositDetails> getFixedDeposits();
}
