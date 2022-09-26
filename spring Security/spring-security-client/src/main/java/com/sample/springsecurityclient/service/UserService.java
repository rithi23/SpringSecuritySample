package com.sample.springsecurityclient.service;


import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.entity.VerificationToken;
import com.sample.springsecurityclient.model.UserModel;

public interface UserService {
    UserEntity registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, UserEntity user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    UserEntity findUserByEmail(String email);

    void createPasswordResetForToken(UserEntity user, String token);
}
