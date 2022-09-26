package com.sample.springsecurityclient.event;

import com.sample.springsecurityclient.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.time.Clock;
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    @Autowired
    private  UserEntity user;
    private String applicationUrl;

    public RegistrationCompleteEvent(UserEntity user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
