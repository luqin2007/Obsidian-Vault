package com.example.mybank.service;

import com.example.mybank.dao.FixedDepositDao;
import com.example.mybank.domain.FixedDepositDetails;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FixedDepositService {

    @Autowired
    private FixedDepositDao fixedDepositDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional("jmsTxManager")
    public int createFixedDeposit(FixedDepositDetails fixedDepositDetails) {
        jmsTemplate.send("emailQueueDestination", session -> {
            TextMessage message = session.createTextMessage();
            message.setText(fixedDepositDetails.getEmail());
            return message;
        });

        int result = fixedDepositDao.createFixedDetail(fixedDepositDetails);

        jmsTemplate.send("aQueueDestination", session -> {
            ObjectMessage message = session.createObjectMessage();
            message.setObject(fixedDepositDetails);
            return message;
        });

        return result;
    }
}
