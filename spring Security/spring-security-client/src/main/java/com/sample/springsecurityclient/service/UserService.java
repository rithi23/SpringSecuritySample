package com.sample.springsecurityclient.service;


import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.model.UserModel;

public interface UserService {
    UserEntity registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, UserEntity user);
}
