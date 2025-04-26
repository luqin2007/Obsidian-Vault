package com.example.mybank.service;

import com.example.mybank.annotation.BankType;
import com.example.mybank.annotation.FundTransfer;
import com.example.mybank.annotation.TransformMode;
import org.springframework.stereotype.Service;

@Service
@FundTransfer(
        transformSpeed = TransformMode.NORMAL,
        bankType = BankType.DIFF)
public class NormalDiffBank implements FundTransferService {
}
