package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private int accId;
    private String authenticationToken;
    //    private String refreshToken;
//    private Instant expiresAt;
    private String username;

    private List<AccountRoleDTO> accountRoleDTO;
//    private List<AccountRoleDTO> accountRoleDTOS;
}
