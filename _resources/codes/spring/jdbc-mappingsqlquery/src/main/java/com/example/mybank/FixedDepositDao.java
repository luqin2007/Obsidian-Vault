package com.example.mybank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FixedDepositDao {

    MappingSqlQuery<FixedDepositDetails> mappingSqlQuery;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        mappingSqlQuery = new FixedDepositDetailsMappingSqlQuery(dataSource);
    }

    public FixedDepositDetails getFixedDepositDetails(int id) {
        return mappingSqlQuery.findObject(id);
    }
}
