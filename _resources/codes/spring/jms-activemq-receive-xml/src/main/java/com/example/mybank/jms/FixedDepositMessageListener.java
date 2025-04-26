package com.example.mybank.jms;

import com.example.mybank.domain.FixedDepositDetails;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;

public class FixedDepositMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            FixedDepositDetails fdDetails = (FixedDepositDetails) objectMessage.getObject();
            System.out.println("Received fixed deposit details: " + fdDetails);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
