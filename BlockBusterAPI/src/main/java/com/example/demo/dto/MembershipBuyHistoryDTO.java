package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipBuyHistoryDTO {
    private Integer id;
    private Date buyTime;
    private Integer moneyIncome;
    private Integer voucher;
    private MembershipDetailDTO membershipDetail;
    private AccountDTO account;
}
