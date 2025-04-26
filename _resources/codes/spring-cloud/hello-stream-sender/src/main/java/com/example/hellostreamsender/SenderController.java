package com.example.hellostreamsender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SenderController {

    @Autowired
    StreamBridge streamBridge;

    @GetMapping("/{msg}")
    public void send(@PathVariable String msg) {
        streamBridge.send("hello", msg);
        System.out.println("Send message " + msg);
    }
}
