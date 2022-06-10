package com.example.demo.DTO;

import java.time.Instant;

public class VerificationTokenDTO {
    private int id;

    private AccountDTO movieAccountInToken;

    private Instant createdTime;

    private String tokenContent;
}
