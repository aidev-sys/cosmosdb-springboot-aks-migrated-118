package com.azure.cosmosdb.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Repository;

class User {

    private Long id;

    private String name;

    private String email;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

@Repository
public class UserRepository {

    private final RabbitTemplate rabbitTemplate;
    private final StreamBridge streamBridge;

    public UserRepository(RabbitTemplate rabbitTemplate, StreamBridge streamBridge) {
        this.rabbitTemplate = rabbitTemplate;
        this.streamBridge = streamBridge;
    }

    public void save(User user) {
        rabbitTemplate.convertAndSend("user.exchange", "user.routing.key", user);
    }

    public void saveWithStreamBridge(User user) {
        streamBridge.send("user-out-0", user);
    }
}