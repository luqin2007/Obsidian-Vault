package com.example.mybank.dao;

import java.util.Locale;

public class FixedDepositDaoFactory {

    public FixedDepositDao getFixedDepositDao(String type) {
        return switch (type.toLowerCase(Locale.ROOT)) {
            case "jdbc" -> new FixedDepositJdbcDao();
            case "hibernate" -> new FixedDepositHibernateDao();
            case "ibatis" -> new FixedDepositIbatisDao();
            default -> new FixedDepositDao();
        };
    }
}