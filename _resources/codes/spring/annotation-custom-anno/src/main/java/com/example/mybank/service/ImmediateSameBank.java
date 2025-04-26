package com.example.mybank.service;

import com.example.mybank.annotation.BankType;
import com.example.mybank.annotation.FundTransfer;
import com.example.mybank.annotation.TransformMode;
import org.springframework.stereotype.Service;

@Service
@FundTransfer(
        transformSpeed = TransformMode.IMMEDIATE,
        bankType = BankType.SAME)
public class ImmediateSameBank implements FundTransferService {
}
