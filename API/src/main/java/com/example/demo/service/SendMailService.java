package com.example.demo.service;


import com.example.demo.model.Account;

public interface SendMailService {
    Boolean signup(Account sdi, String token);

    Boolean create(Account sdi, String password);

    void forgotPassword(Account sdi, String password);
}
