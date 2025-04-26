package com.example.mybank.service;

import com.example.mybank.dao.FixedDepositDao;
import com.example.mybank.domain.FixedDepositDetails;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class FixedDepositService {

    public int createFixedDeposit(FixedDepositDetails fixedDepositDetails) {
        FixedDepositDetails fdd = getTransactionTemplate().execute(status -> {
            JmsTemplate jmsTemplate = getJmsTemplate();

            // TODO: 通过 emailQueueDestination 通道发送邮件信息
            jmsTemplate.send("emailQueueDestination", session -> {
                TextMessage message = session.createTextMessage();
                message.setText(fixedDepositDetails.getEmail());
                return message;
            });

            getFixedDepositDao().createFixedDetail(fixedDepositDetails);

            // TODO: 通过 aQueueDestination 通道发送修改信息
            jmsTemplate.send("aQueueDestination", session -> {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(fixedDepositDetails);
                return message;
            });

            return fixedDepositDetails;
        });

        return fdd.getId();
    }

    public abstract JmsTemplate getJmsTemplate();

    public abstract FixedDepositDao getFixedDepositDao();

    public abstract TransactionTemplate getTransactionTemplate();
}
