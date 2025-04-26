package com.example.mybank;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FixedDepositDetailsMappingSqlQuery extends MappingSqlQuery<FixedDepositDetails> {
    private static final String SQL_QUERY_DEPOSIT_BY_ID = "select * from public.fixed_deposit_details where fixed_deposit_id = ?";

    public FixedDepositDetailsMappingSqlQuery(DataSource ds) {
        super(ds, SQL_QUERY_DEPOSIT_BY_ID);
        setParameters(new SqlParameter("id", java.sql.Types.INTEGER));
    }

    @Override
    protected FixedDepositDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        FixedDepositDetails fixedDepositDetails = new FixedDepositDetails();
        var aid = rs.getInt("account_id");
        var dc = rs.getDate("fd_creation_date");
        var amount = rs.getString("amount");
        var tenure = rs.getInt("tenure");
        var active = rs.getString("active").equals("Y");
        fixedDepositDetails.setBankAccountId(aid);
        fixedDepositDetails.setDepositAmount(Integer.parseInt(amount));
        fixedDepositDetails.setCreationDate(dc);
        fixedDepositDetails.setTenure(tenure);
        fixedDepositDetails.setActive(active);
        return fixedDepositDetails;
    }
}
