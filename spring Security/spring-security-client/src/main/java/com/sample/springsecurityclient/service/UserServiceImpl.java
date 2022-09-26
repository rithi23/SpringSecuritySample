package com.sample.springsecurityclient.service;

import com.sample.springsecurityclient.entity.PasswordResetToken;
import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.entity.VerificationToken;
import com.sample.springsecurityclient.model.UserModel;
import com.sample.springsecurityclient.repository.PasswordResetTokenRepository;
import com.sample.springsecurityclient.repository.UserRepository;
import com.sample.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@Component
class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

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

    @Override
    public String validateVerificationToken(String token) {
         VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
         if(verificationToken == null) {
             return null;
         }
         UserEntity user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
         if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
             verificationTokenRepository.delete(verificationToken);
             return "expired";
         }

         user.setEnabled(true);
         userRepository.save(user);
         return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetForToken(UserEntity user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if(passwordResetToken == null) {
            return null;
        }
        UserEntity user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public UserEntity getUserByPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token).getUser();
    }

    @Override
    public void changePassword(UserEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(UserEntity user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}