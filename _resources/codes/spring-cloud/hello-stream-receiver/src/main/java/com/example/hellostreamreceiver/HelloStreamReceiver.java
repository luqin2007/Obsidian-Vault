package com.example.hellostreamreceiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class HelloStreamReceiver {

    public static void main(String[] args) {
        SpringApplication.run(HelloStreamReceiver.class, args);
    }
}
