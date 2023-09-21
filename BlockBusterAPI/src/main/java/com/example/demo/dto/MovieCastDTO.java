package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieCastDTO {
    private int id;
    private String avatar;
    private String name;
    private String story;
    private Date birthday;
}
