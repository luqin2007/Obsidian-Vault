package com.example.mybank.service;

import com.example.mybank.dao.FixedDepositRepository;
import com.example.mybank.domain.BankAccountDetails;
import com.example.mybank.domain.FixedDepositDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FixedDepositService {

    @Autowired
    private FixedDepositRepository fixedDepositRepository;

    public List<FixedDepositDetails> findExample(FixedDepositDetails fdd) {
        // 忽略 id 属性
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("fixed_deposit_id");
        Example<FixedDepositDetails> example = Example.of(fdd, matcher);
        return fixedDepositRepository.findAll(example);
    }
}
