package com.example.mybank.dao;

import java.util.Locale;

public class FixedDepositDaoFactory {

    public FixedDepositDao getFixedDepositDao(String type) {
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "jdbc" -> new FixedDepositJdbcDao();
            default -> new FixedDepositDao();
        };
    }
}