package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDirectorDTO {
    private int id;
    private String avatar;
    private String name;
    private String story;
    private Date birthday;
}
