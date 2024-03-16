package com.springsecurity.security.implementation;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsecurity.security.entity.PasswordResetToken;
import com.springsecurity.security.entity.UserEntity;
import com.springsecurity.security.entity.VerificationToken;
import com.springsecurity.security.model.UserModel;
import com.springsecurity.security.repository.PasswordResetTokenRepository;
import com.springsecurity.security.repository.UserRepository;
import com.springsecurity.security.repository.VerificationTokenRepository;
import com.springsecurity.security.service.UserService;


@Service
public class UserServiceImplementation implements UserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository verificationTokenRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder,VerificationTokenRepository verificationTokenRepository,PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository=verificationTokenRepository;
        this.passwordResetTokenRepository=passwordResetTokenRepository;
    }

    @Override
    public UserEntity registerUser(UserModel userModel) {
        UserEntity user=new UserEntity();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("User");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(user);
        return user;

    }

    @Override
    public void saveVerficationTokenForUser(String token, UserEntity user) {
        VerificationToken verificationToken=new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateRegistrationToken(String token) {
        VerificationToken verificationToken=verificationTokenRepository.findByToken(token);
        if(!(verificationToken instanceof VerificationToken)){
            return "null";
        }
        if(token==null){
            return "null";
        }
        UserEntity user=verificationToken.getUser();
        Calendar cal=Calendar.getInstance();

        //database time - current time <=0
        if(verificationToken.getExpirationTime().getTime()-cal.getTime().getTime() <=0){
            //delete token
            verificationTokenRepository.delete(verificationToken);
            return "expiry";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String token) {
        VerificationToken verificationToken=verificationTokenRepository.findByToken(token);
        //set new token
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }



    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUSer(UserEntity user, String token) {
        //do similiar as verfication token
        PasswordResetToken passwordResetToken=new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validtaePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken=passwordResetTokenRepository.findByToken(token);
        if(!(passwordResetToken instanceof PasswordResetToken)){
            return "null";
        }
        if(token==null){
            return "null";
        }
        UserEntity user=passwordResetToken.getUser();
        Calendar cal=Calendar.getInstance();

        //database time - current time <=0
        if(passwordResetToken.getExpirationTime().getTime()-cal.getTime().getTime() <=0){
            //delete token
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expiry";
        }

        // user.setEnabled(true);
        // userRepository.save(user);
        return "valid";
    }

    @Override
    public Optional<UserEntity> getUserByPAsswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(UserEntity userEntity, String newPAssword) {
        userEntity.setPassword(passwordEncoder.encode(newPAssword));
        userRepository.save(userEntity);
    }

    @Override
    public boolean checkIfValidOldPassword(UserEntity user, String oldPAssword) {
        return passwordEncoder.matches(oldPAssword, user.getPassword());
    }
    
}
