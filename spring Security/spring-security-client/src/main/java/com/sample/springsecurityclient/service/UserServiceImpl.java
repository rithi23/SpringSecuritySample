package com.sample.springsecurityclient.service;

import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.entity.VerificationToken;
import com.sample.springsecurityclient.model.UserModel;
import com.sample.springsecurityclient.repository.UserRepository;
import com.sample.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Override
    public UserEntity registerUser(UserModel userModel) {
         UserEntity user = new UserEntity();
         user.setFirstName(userModel.getFirstName());
         user.setLastName( (userModel.getLastName()));
         user.setEmail(userModel.getEmail());
         user.setRole("user");
         user.setPassword(passwordEncoder.encode(userModel.getPassword()));
         return userRepository.save(user);
    }

    @Override
    public void saveVerificationTokenForUser(String token, UserEntity user) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }
}