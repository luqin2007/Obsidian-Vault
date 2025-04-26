package com.example.mybank.jms;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public abstract class EmailMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String email = textMessage.getText();
            SimpleMailMessage mailMessage = getRequestReceivedTemplate();
            mailMessage.setTo(email);
            getMailSender().send(mailMessage);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract JavaMailSender getMailSender();

    public abstract SimpleMailMessage getRequestReceivedTemplate();
}
