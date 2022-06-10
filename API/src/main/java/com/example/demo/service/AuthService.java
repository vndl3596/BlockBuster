package com.example.demo.service;

import com.example.demo.DTO.AuthenticationResponse;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.RegisterRequest;
import com.example.demo.exception.MailException;
import com.example.demo.exception.UsernameExitException;
import com.example.demo.model.Account;

public interface AuthService {
    Account signup(RegisterRequest registerRequest) throws MailException, UsernameExitException;

    void verifyAccount(String token);

    AuthenticationResponse login(LoginRequest loginRequest);
}
