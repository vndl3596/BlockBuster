package com.example.blockbuster.dto;

import java.time.Instant;

public class VerificationTokenDTO {
    private int id;

    private AccountDTO movieAccountInToken;

    private Instant createdTime;

    private String tokenContent;
}
