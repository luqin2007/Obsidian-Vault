package com.example.mybank.dao;

import com.example.mybank.domain.CustomerRegistrationDetails;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegistrationDao {

    private static final String SQL_INSERT_CUSTOMER_REGISTRATION =
            "insert into public.customer_registration_details(account_number, address, card_number) " +
            "values (:account, :address, :number)";

    private TransactionTemplate transactionTemplate;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public void registerCustomer(CustomerRegistrationDetails customerRegistrationDetails) {
        transactionTemplate.executeWithoutResult(status -> {
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("account", customerRegistrationDetails.getAccountNumber());
            arguments.put("address", customerRegistrationDetails.getAddress());
            arguments.put("number", customerRegistrationDetails.getCardNumber());
            jdbcTemplate.update(SQL_INSERT_CUSTOMER_REGISTRATION, arguments);
        });
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
