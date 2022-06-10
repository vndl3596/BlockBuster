package com.example.blockbuster.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginAcc {
    private String username;
    private String password;
}
