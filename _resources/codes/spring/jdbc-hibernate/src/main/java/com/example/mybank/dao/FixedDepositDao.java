package com.example.mybank.dao;

import com.example.mybank.domain.FixedDepositDetails;
import jakarta.annotation.PreDestroy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FixedDepositDao {

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

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.session = sessionFactory.openSession();
    }

    @PreDestroy
    public void destroy() {
        session.close();
    }
}
