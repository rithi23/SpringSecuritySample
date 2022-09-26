package com.sample.springsecurityclient.controller;

import com.sample.springsecurityclient.entity.UserEntity;
import com.sample.springsecurityclient.entity.VerificationToken;
import com.sample.springsecurityclient.event.RegistrationCompleteEvent;
import com.sample.springsecurityclient.model.PasswordModel;
import com.sample.springsecurityclient.model.UserModel;
import com.sample.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/hello")
    public String hello() {
        return "hi ";
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, HttpServletRequest request) {
        UserEntity user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user,applicationUrl(request)));
        return "successfully completed";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            return "user verified successfully";
        }
        return "bad user";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        UserEntity user = verificationToken.getUser();
        resendVerificationTokenMail(user,applicationUrl(request), verificationToken);
        return "verification link sent";
        
    }

    private void resendVerificationTokenMail(UserEntity user, String applicationUrl, VerificationToken verificationToken) {

        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken() ;

        log.info("Click the link to verify your account :" + url);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        UserEntity user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetForToken(user,token);
            url = passwordResetTokenMail(applicationUrl(request), token);
        }
        return  url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);
        if(!result.equalsIgnoreCase("valid")) {
            return "Invalid token";
        }
        UserEntity user = userService.getUserByPasswordResetToken(token);
        userService.changePassword(user, passwordModel.getNewPassword());
        return "Password changed successfully";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        UserEntity user = userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())) {
            return "invalid old password";
        }
        userService.changePassword(user,passwordModel.getNewPassword());
        return "password changed successfully";
    }

    private String passwordResetTokenMail(String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;

        log.info("Click the link to reset your password :" + url);
        return  url;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
