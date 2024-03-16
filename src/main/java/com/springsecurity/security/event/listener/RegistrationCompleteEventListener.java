package com.springsecurity.security.event.listener;

import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.springsecurity.security.entity.UserEntity;
import com.springsecurity.security.event.RegistrationCompleteEvent;
import com.springsecurity.security.service.UserService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{

    private UserService userService;
    // public static final String ANSI_RESET = "\u001B[0m";
    // public static final String ANSI_RED = "\u001B[31m";
    // public static final String ANSI_GREEN = "\u001B[32m";
    // public static final String ANSI_YELLOW = "\u001B[33m";
    // public static final String ANSI_BLUE = "\u001B[34m";

    public RegistrationCompleteEventListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // cerate a verification token for user with link
        UserEntity user=event.getUser();
        String token=UUID.randomUUID().toString();
        userService.saveVerficationTokenForUser(token,user);
        //send mail to user
        // String url=event.getApplicationURL() + "/verifyRegistration?token=" + token;
        String url="localhost:8000" + "/verifyRegistration?token=" + token;



        // consider this has been sent to email
        log.info("click the url to verify your account : {}",url);
        // log.info(ANSI_RED + "This text is red." + ANSI_RESET);
        // log.info(ANSI_GREEN + "This text is green." + ANSI_RESET);
        // log.info(ANSI_BLUE + "This text is blue." + ANSI_RESET);

    }
    
}
