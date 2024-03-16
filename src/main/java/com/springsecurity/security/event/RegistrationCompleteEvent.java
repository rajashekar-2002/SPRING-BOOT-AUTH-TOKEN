package com.springsecurity.security.event;

import org.springframework.context.ApplicationEvent;

import com.springsecurity.security.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent{

    private UserEntity user;
    private String applicationURL;

    public RegistrationCompleteEvent(UserEntity user,String applicationURL) {
        super(user);
        this.applicationURL=applicationURL;
        this.user=user;
    }
    
}
