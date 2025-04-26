package com.example.mybank.service;

import com.example.mybank.domain.FixedDepositDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FixedDepositServiceImpl implements FixedDepositService {

    @Override
    public List<FixedDepositDetails> getFixedDeposits() {
        List<FixedDepositDetails> list = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            FixedDepositDetails fdd = new FixedDepositDetails();
            fdd.setId(i);
            fdd.setBankAccountId(i);
            fdd.setTenure((i + 1) * 6);
            fdd.setDepositAmount((i + 1) * 1000);
            fdd.setActive((i % 2 == 0) ? "Y" : "N");
            fdd.setEmail(String.format("email%d@email.com", i));
            fdd.setMaturityDate(new Date());
            fdd.setCreationDate(new Date());
            list.add(fdd);
        }
        return List.copyOf(list);
    }
}
