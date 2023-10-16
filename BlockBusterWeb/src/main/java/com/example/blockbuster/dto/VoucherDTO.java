package com.example.blockbuster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {
    private Integer id;
    private String voucherCode;
    private String voucherName;
    private Date timeFrom;
    private Date timeTo;
    private Boolean status;
    private int reduce;
    private int type;
    private MembershipDetailDTO packageReduce;
}
