package com.example.mybank;

import com.example.mybank.annotation.BankType;
import com.example.mybank.annotation.FundTransfer;
import com.example.mybank.annotation.TransformMode;
import com.example.mybank.service.FundTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FundTransferProcessor {

    @Autowired
    @FundTransfer(transformSpeed = TransformMode.IMMEDIATE, bankType = BankType.SAME)
    private FundTransferService sameBankImmediateFundTransferService;

    @Autowired
    @FundTransfer(transformSpeed = TransformMode.IMMEDIATE, bankType = BankType.DIFF)
    private FundTransferService diffBankImmediateFundTransferService;

    @Autowired
    @FundTransfer(transformSpeed = TransformMode.NORMAL, bankType = BankType.SAME)
    private FundTransferService sameBankNormalFundTransferService;

    @Autowired
    @FundTransfer(transformSpeed = TransformMode.NORMAL, bankType = BankType.DIFF)
    private FundTransferService diffBankNormalFundTransferService;

    @Override
    public String toString() {
        return "FundTransferProcessor{" +
                " \n    sameBankImmediateFundTransferService=" + sameBankImmediateFundTransferService +
                ",\n    diffBankImmediateFundTransferService=" + diffBankImmediateFundTransferService +
                ",\n    sameBankNormalFundTransferService=" + sameBankNormalFundTransferService +
                ",\n    diffBankNormalFundTransferService=" + diffBankNormalFundTransferService +
                "\n}";
    }
}
