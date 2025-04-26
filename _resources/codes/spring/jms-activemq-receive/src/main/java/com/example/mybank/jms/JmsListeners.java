package com.example.mybank.jms;

import com.example.mybank.domain.FixedDepositDetails;
import jakarta.jms.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@JMSConnectionFactory("jmsListenerContainerFactory")
public class JmsListeners {

    @JmsListener(id = "fixedDepositsMessageListener", destination = "aQueueDestination")
    public void onReceiveFixedDepositDetails(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            FixedDepositDetails fdDetails = (FixedDepositDetails) objectMessage.getObject();
            System.out.println("Received fixed deposit details: " + fdDetails);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    @JmsListener(id = "emailMessageListener", destination = "emailQueueDestination")
    public void onReceiveEmailMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        System.out.println("Received email message: " + textMessage);
    }
}