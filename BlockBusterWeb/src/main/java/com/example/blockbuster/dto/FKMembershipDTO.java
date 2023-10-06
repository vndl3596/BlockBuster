package com.example.blockbuster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FKMembershipDTO {
    private int membershipId;
    private int accountId;
    private Date toTime;
}
