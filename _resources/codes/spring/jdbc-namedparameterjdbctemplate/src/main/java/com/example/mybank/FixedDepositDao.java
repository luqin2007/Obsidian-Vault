package com.example.mybank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.StringReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Map;

@Repository
public class FixedDepositDao {

    private static final String SQL_CREATE_FIXED_DETAIL =
            "insert into public.fixed_deposit_details(account_id, fd_creation_date, amount, tenure, active) " +
                    "values (:account, :creation_date, :amount, :tenure, :active)";
    private static final String SQL_QUERY_DEPOSIT_BY_ID =
            "select * from public.fixed_deposit_details where fixed_deposit_id = :id";

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public int createFixedDetail(FixedDepositDetails fixedDepositDetails) {
        Map<String, Object> parameters = Map.of(
                "account", fixedDepositDetails.getBankAccountId(),
                "creation_date", new Date(fixedDepositDetails.getCreationDate().getTime()),
                "amount", fixedDepositDetails.getDepositAmount(),
                "tenure", fixedDepositDetails.getTenure(),
                "active", fixedDepositDetails.isActive() ? "Y" : "N");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_CREATE_FIXED_DETAIL, parameterSource, keyHolder, new String[]{"fixed_deposit_id"});
        return keyHolder.getKey().intValue();
    }

    public FixedDepositDetails getFixedDetail(int fixedDepositId) {
        SqlParameterSource parameters = new MapSqlParameterSource("id", fixedDepositId);
        return jdbcTemplate.queryForObject(SQL_QUERY_DEPOSIT_BY_ID, parameters, (rowSet, rowNum) -> {
            FixedDepositDetails fixedDepositDetails = new FixedDepositDetails();
            var aid = rowSet.getInt("account_id");
            var dc = rowSet.getDate("fd_creation_date");
            var amount = rowSet.getString("amount");
            var tenure = rowSet.getInt("tenure");
            var active = rowSet.getString("active").equals("Y");
            fixedDepositDetails.setBankAccountId(aid);
            fixedDepositDetails.setDepositAmount(Integer.parseInt(amount));
            fixedDepositDetails.setCreationDate(dc);
            fixedDepositDetails.setTenure(tenure);
            fixedDepositDetails.setActive(active);
            return fixedDepositDetails;
        });
    }
}
