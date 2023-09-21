package com.example.demo.payload;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String roleName;
}
