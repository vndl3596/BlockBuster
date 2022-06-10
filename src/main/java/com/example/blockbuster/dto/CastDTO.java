package com.example.blockbuster.dto;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CastDTO {
    private int id;
    private String avatar;
    private String name;
    private String story;
    private LocalDate birthday;
}
