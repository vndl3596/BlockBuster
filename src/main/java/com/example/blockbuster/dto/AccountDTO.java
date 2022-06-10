package com.example.blockbuster.dto;

import com.example.blockbuster.dto.address.TownDTO;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private int id;
    private String username;
    private String password;
    private boolean enabled;
    private String email;
    private String avatar;
    private String firstname;
    private String lastname;
    private Date birthday;
    private Integer town;
    private String address;
    private String phoneNumber;
    private boolean gender;
}
