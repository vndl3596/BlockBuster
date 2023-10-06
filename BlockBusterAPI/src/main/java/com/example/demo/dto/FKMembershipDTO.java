package com.example.demo.dto;

import com.example.demo.model.Account;
import com.example.demo.model.Membership;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FKMembershipDTO {
    private int membershipId;
    private int accountId;
    private Date toTime;
}
