package com.example.mybank.dao;

import com.example.mybank.domain.FixedDepositDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class FixedDepositDao {

    public static final String HQL = "from FixedDepositDetails fd where fd.fixedDepositId=";
    private Session session;

    public FixedDepositDetails getFixedDeposit(int fixedDepositId) {
        String hql = HQL + fixedDepositId;
        return session.createQuery(hql, FixedDepositDetails.class)
                .uniqueResult();
    }

    public int createFixedDeposit(FixedDepositDetails fd) {
        session.persist(fd);
        return fd.getFixedDepositId();
    }

    public abstract SessionFactory getSessionFactory();

    public void init() {
        session = getSessionFactory().openSession();
    }

    public void destroy() {
        session.close();
    }
}
