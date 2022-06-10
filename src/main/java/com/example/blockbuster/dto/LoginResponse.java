package com.example.blockbuster.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private int accId;
    private String authenticationToken;
    private String username;
    private List<AccountRoleDTO> accountRoleDTO;
}
