package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieCastDTO {
    private int id;
    private String avatar;
    private String name;
    private String story;
    private LocalDate birthday;
}
