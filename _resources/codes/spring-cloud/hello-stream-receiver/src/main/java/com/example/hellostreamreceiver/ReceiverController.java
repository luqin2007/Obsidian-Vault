package com.example.hellostreamreceiver;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReceiverController {

    public static String receivedMessage = "";

    @GetMapping("/")
    public String index() {
        return receivedMessage;
    }

    @KafkaListener(topics = "hello", groupId = "hello-group")
    public void handleMessage(String message) {
        ReceiverController.receivedMessage = message;
        System.out.println("Receive message " + message);
    }
}
