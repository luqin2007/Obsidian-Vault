package com.example.mybank.jms;

import com.example.mybank.domain.FixedDepositDetails;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class FixedDepositMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            FixedDepositDetails fdDetails = (FixedDepositDetails) objectMessage.getObject();
            SimpleMailMessage mailTemplate = getProcessedReceivedTemplate();
            // 使用 MimeMessageHelper 创建 MimeMessage
            MimeMessage mimeMessage = getMailSender().createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(fdDetails.getEmail());
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(mailTemplate.getText());
            helper.setFrom(mailTemplate.getFrom());
            getMailSender().send(helper.getMimeMessage());
            // 使用 MimeMessagePreparator 创建 MimeMessage
            getMailSender().send(mimemessage -> {
                // setTo
                mimemessage.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(fdDetails.getEmail()));
                mimemessage.setSubject(mailTemplate.getSubject());
                mimemessage.setText(mailTemplate.getText());
                mimemessage.setFrom(mailTemplate.getFrom());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract JavaMailSender getMailSender();

    public abstract SimpleMailMessage getProcessedReceivedTemplate();
}
