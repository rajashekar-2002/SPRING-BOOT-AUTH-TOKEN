package com.springsecurity.security.service;

import java.util.Optional;

import com.springsecurity.security.entity.UserEntity;
import com.springsecurity.security.entity.VerificationToken;
import com.springsecurity.security.model.UserModel;

public interface UserService {
    public UserEntity registerUser(UserModel userModel);

    public void saveVerficationTokenForUser(String token, UserEntity user);

    public String validateRegistrationToken(String token);

    public VerificationToken generateNewVerificationToken(String token);

    public UserEntity findUserByEmail(String email);

    public void createPasswordResetTokenForUSer(UserEntity user, String token);

    public String validtaePasswordResetToken(String token);

    public Optional<UserEntity> getUserByPAsswordResetToken(String token);

    public void changePassword(UserEntity userEntity, String newPAssword);

    public boolean checkIfValidOldPassword(UserEntity user, String oldPAssword);
}
