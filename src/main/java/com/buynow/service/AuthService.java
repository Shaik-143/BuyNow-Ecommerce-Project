package com.buynow.service;

import com.buynow.exception.SellerException;
import com.buynow.exception.UserException;
import com.buynow.request.LoginRequest;
import com.buynow.request.SignupRequest;
import com.buynow.response.AuthResponse;

import jakarta.mail.MessagingException;

public interface AuthService { 

    void sentLoginOtp(String email) throws UserException, MessagingException;
    String createUser(SignupRequest req) throws SellerException;
    AuthResponse signin(LoginRequest req) throws SellerException, UserException; // here 5:10 vid time

} 