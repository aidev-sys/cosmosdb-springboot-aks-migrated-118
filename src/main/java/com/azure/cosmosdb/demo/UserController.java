package com.azure.cosmosdb.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public UserController() throws Exception {
    }

    // Upsert - create if not exists, update if exists
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {

        System.out.println("add/update " + user);

        rabbitTemplate.convertAndSend("user.exchange", "user.created", user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {

        System.out.println("searching user " + email);

        // Simulate finding user from RabbitMQ or cache
        User foundUser = new User();
        foundUser.setEmail(email);
        foundUser.setName("Sample User");
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("listing all users...");

        List<User> result = new ArrayList<>();
        // Simulate fetching all users from RabbitMQ or cache
        User user1 = new User();
        user1.setEmail("test@example.com");
        user1.setName("Test User");
        result.add(user1);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // replace existing item (not upsert)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> replace(@RequestBody User user) {

        System.out.println("replacing user " + user.getEmail());

        // Simulate checking if user exists
        boolean exists = true;
        if (!exists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        rabbitTemplate.convertAndSend("user.exchange", "user.updated", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email) {

        System.out.println("deleting user " + email);

        rabbitTemplate.convertAndSend("user.exchange", "user.deleted", email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}