package com.springsecurity.security.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.security.entity.UserEntity;
import com.springsecurity.security.entity.VerificationToken;
import com.springsecurity.security.event.RegistrationCompleteEvent;
import com.springsecurity.security.implementation.UserServiceImplementation;
import com.springsecurity.security.model.PasswordModel;
import com.springsecurity.security.model.UserModel;
import com.springsecurity.security.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RegistrationController {

    private UserService userService;
    private ApplicationEventPublisher publisher;
    private UserServiceImplementation userServiceImplementation;

    public RegistrationController(UserService userService, ApplicationEventPublisher publisher,UserServiceImplementation userServiceImplementation) {
        this.userService = userService;
        this.publisher = publisher;
        this.userServiceImplementation=userServiceImplementation;
    }

    
    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel,final HttpServletRequest request){
        UserEntity user=userService.registerUser(userModel);
        // seperate sending mail event using application event publish
        publisher.publishEvent(new RegistrationCompleteEvent(
                                user,
                                applicationURL(request)));    //update application url here

        return "success";
    }

    private String applicationURL(HttpServletRequest request) {
        return "http://" + 
                request.getServerName() +
                request.getServerPort() +
                request.getContextPath();
    }






    @GetMapping("/verifyRegistration")
    public String verifytoken(@RequestParam("token") String token ){
        String result=userService.validateRegistrationToken(token);
        log.info(token);
        if(result.equalsIgnoreCase("valid")){
            return "user verfied succesfully ... ";
        }
        //delete token after expiration time
        return "very bad user .. ";
    }


    // also add in web security config
    @GetMapping("/resendToken")
    public String resendToken(@RequestParam("token") String oldToekn, HttpServletRequest request){
        VerificationToken verificationToken=userService.generateNewVerificationToken(oldToekn);
        UserEntity user=verificationToken.getUser();
        resendVerficationToken(user,applicationURL(request),verificationToken);
        return "Verfication Link has been set";

    }


    private void resendVerficationToken(UserEntity user, String applicationURL,VerificationToken verificationToken) {
        //make use of getapplicationURL or directly write localhost it has : missing for port 
        String url="localhost:8000" + "/verifyRegistration?token=" + verificationToken.getToken();

        // consider this has been sent to email
        log.info("Token has been recreated to verify account : {}",url);

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,HttpServletRequest request){
        // find user using email > create token > send mail
        // only mail is taken from user
        UserEntity user=userService.findUserByEmail(passwordModel.getEmail());
        String url="";
        if(user!=null){
            String token=UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUSer(user,token);
            url=passwordResetTokenMail(user,applicationURL(request),token);

        }
        return url;
    }


    private String passwordResetTokenMail(UserEntity user, String applicationURL, String token) {
        //USE APPLIACTIONURL OR DIRECTLY WRITE LOCALHOST
        String url="localhost:8000" + "/savePassword?token=" + token;
        //make use of getapplicationURL or directly write localhost it has : missing for port 

        // consider this has been sent to email
        log.info("LINK TO CHANGE UR PASSWORD : {}",url);
        return url;
    }


    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel){
        //UPDATE NEW PASSWORD FROM POSTMAN
        String result=userService.validtaePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")){
            return "INVALID TOKEN .. ";
        }
        Optional<UserEntity> user=userService.getUserByPAsswordResetToken(token);
        if(user.isPresent()){
            userServiceImplementation.changePassword(user.get(),passwordModel.getNewPAssword());
            //also upadte newpassword in passwordmodel with user model password for /changePassword
            return "VALID TOKEN";
        }else{
            return "INVALID TOKEN .. ";
        }
        

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        //compare userEntity password and oldpassword in passwordmodel
        UserEntity user=userService.findUserByEmail(passwordModel.getEmail());
        if(userService.checkIfValidOldPassword(user,passwordModel.getOldPAssword())){
            return "INVALID OLD PASSWORD .. ";
        }

        //SAVE NEW PASSWORD
        userService.changePassword(user, passwordModel.getNewPAssword());
        return " password has been changed";
    }


}
