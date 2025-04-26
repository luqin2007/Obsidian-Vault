package com.example.mybank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.StringReader;
import java.sql.Date;
import java.sql.PreparedStatement;

@Repository
public class FixedDepositDao {

    public static final String SQL_CREATE_FIXED_DETAIL = "insert into public.fixed_deposit_details(account_id, fd_creation_date, amount, tenure, active) values (?, ?, ?, ?, ?)";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int createFixedDetail(FixedDepositDetails fdd) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_FIXED_DETAIL, new String[] {"fixed_deposit_id"});
            ps.setInt(1, fdd.getBankAccountId());
            ps.setDate(2, new Date(fdd.getCreationDate().getTime()));
            ps.setInt(3, fdd.getDepositAmount());
            ps.setInt(4, fdd.getTenure());
            ps.setCharacterStream(5, new StringReader(fdd.isActive() ? "Y" : "N"));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }
}
