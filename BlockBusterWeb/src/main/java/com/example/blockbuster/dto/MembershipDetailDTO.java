package com.example.blockbuster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDetailDTO {
    private Integer id;
    private Integer year;
    private Integer month;
    private Integer day;
    private int price;
    private int membership;
}
