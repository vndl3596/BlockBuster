package com.example.blockbuster.dto;

import com.example.blockbuster.dto.address.TownDTO;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String avatar;
    private String firstname;
    private String lastname;
    private TownDTO idTown;
    private String address;
    private Date birthday;
    private boolean enabled;
    private boolean gender;

}
