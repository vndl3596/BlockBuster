package com.example.demo.DTO;

import com.example.demo.model.address.Town;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "email cannot be empty")
    @Pattern(regexp = "^[a-z0-9](.?[a-z0-9]){0,}@g(oogle)?mail.com$", message = "Invalid email")
    private String email;

    private String avatar;

    @NotBlank(message = "Firstname cannot be empty")
    private String firstname;

    @NotBlank(message = "Lastname cannot be empty")
    private String lastname;

    private Town idTown;

    private String address;

    @NotNull(message = "Birthday cannot be empty")
    @PastOrPresent(message = "Not Required")
    private Date birthday;

    private boolean enabled;

    @NotNull(message = "Gender cannot be empty")
    private boolean gender;

}
