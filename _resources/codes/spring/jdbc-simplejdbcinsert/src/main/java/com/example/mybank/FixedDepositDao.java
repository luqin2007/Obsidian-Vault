package com.example.mybank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.Map;

@Repository
public class FixedDepositDao {

    SimpleJdbcInsert jdbcInsert;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("public_fixed_deposit_details")
                .usingGeneratedKeyColumns("fixed_deposit_id");
    }

    public int createFixedDetail(FixedDepositDetails fixedDepositDetails) {
        Map<String, Object> parameters = Map.of(
                "account_id", fixedDepositDetails.getBankAccountId(),
                "fd_creation_date", new Date(fixedDepositDetails.getCreationDate().getTime()),
                "amount", fixedDepositDetails.getDepositAmount(),
                "tenure", fixedDepositDetails.getTenure(),
                "active", fixedDepositDetails.isActive() ? "Y" : "N");
        return jdbcInsert.executeAndReturnKey(parameters).intValue();
    }
}
