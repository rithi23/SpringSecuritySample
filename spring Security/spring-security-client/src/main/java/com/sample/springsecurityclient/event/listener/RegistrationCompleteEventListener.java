package com.sample.springsecurityclient.event.listener;

import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.event.RegistrationCompleteEvent;
import com.sample.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID ;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);

        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        log.info("Click the link to verify your account :" + url);
    }
}
