package com.example.mybank;

import com.example.mybank.dao.FixedDepositDao;
import com.example.mybank.domain.FixedDepositDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        FixedDepositDao dao = (FixedDepositDao) context.getBean("fixedDepositDao");
        FixedDepositDetails fd = dao.getFixedDeposit(3);
        fd.setAAmount(fd.getAAmount() + 1000);
        int id = dao.createFixedDeposit(fd);
        System.out.println(id);
    }
}
