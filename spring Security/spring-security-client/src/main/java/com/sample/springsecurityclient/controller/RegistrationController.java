package com.sample.springsecurityclient.controller;

import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.event.RegistrationCompleteEvent;
import com.sample.springsecurityclient.model.UserModel;
import com.sample.springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/hello")
    public String hello() {
        return "hi ";
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        UserEntity user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
        return "successfully completed";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();

    }
}
