package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Subscriber;
import com.example.test.domain.User;
import com.example.test.service.SubscriberService;
import com.example.test.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;
    private final UserService userService;

    public SubscriberController(SubscriberService subscriberService, UserService userService) {
        this.subscriberService = subscriberService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber entity) throws BadRequestException {

        User user = userService.getUserByUsername(entity.getEmail());
        if (user == null) {
            throw new BadRequestException("User not found");
        }

        if (user.getName().equals(entity.getName()) == false) {
            throw new BadRequestException("User not found");
        }

        Boolean isExist = subscriberService.findByUserEmail(entity.getEmail());

        if (isExist) {
            throw new BadRequestException("Subscriber already exist");
        }

        Subscriber subscriber = subscriberService.create(entity);

        return new ResponseEntity<>(subscriber, HttpStatus.CREATED);
    }

}
