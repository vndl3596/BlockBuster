package com.example.blockbuster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDTO {
    private Integer id;
    private String name;
    private String detail;
    private List<MembershipDetailDTO> membershipDetails;
}
