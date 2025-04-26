package com.example.mybank.dao;

import com.example.mybank.domain.FixedDepositDetails;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public class FixedDepositDao {

    private final List<FixedDepositDetails> details = new ArrayList<>();

    /**
     * 存储存款信息
     * @param fixedDepositDetails 存款信息
     * @return 存款 id
     */
    public int createFixedDetail(FixedDepositDetails fixedDepositDetails) {
        int id = details.size();
        fixedDepositDetails.setId(id);
        details.add(fixedDepositDetails);
        return id;
    }
}
