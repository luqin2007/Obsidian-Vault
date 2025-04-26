package com.example.shopping.mq;

import com.example.shopping.model.UserDto;
import com.example.shopping.model.UserMq;
import com.example.shopping.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserMessageListener {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "user-topic", groupId = "userservice")
    public void onUserMessage(String message) throws JsonProcessingException {
        System.out.println("Receive user message " + message);
        UserMq user = objectMapper.readValue(message, UserMq.class);
        switch (user.getType()) {
            case UserMq.USER_UPDATED -> userService.save(new UserDto(user.getId(), user.getName(), user.getAvatar(), 0));
            case UserMq.USER_DELETED -> userService.delete(user.getId());
        }
    }
}
