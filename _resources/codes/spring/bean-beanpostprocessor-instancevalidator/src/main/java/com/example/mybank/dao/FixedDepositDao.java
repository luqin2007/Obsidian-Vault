package com.example.mybank.dao;

import com.example.mybank.InstanceValidator;

public class FixedDepositDao implements InstanceValidator {

    // 模拟初始化失败
    private Object connection = null;

    @Override
    public void validateInstance() {
        System.out.println("FixedDepositJdbcDao: Validating instance");
        if (connection == null) {
            System.out.println("FixedDepositJdbcDao: Failed to obtain DatabaseConnection instance.");
        }
    }
}
