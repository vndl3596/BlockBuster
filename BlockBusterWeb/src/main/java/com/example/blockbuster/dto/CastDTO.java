package com.example.blockbuster.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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
    private Date birthday;
}
