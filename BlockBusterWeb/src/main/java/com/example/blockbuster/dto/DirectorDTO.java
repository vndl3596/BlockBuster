package com.example.blockbuster.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DirectorDTO {
    private int id;
    private String avatar;
    private String name;
    private String story;
    private Date birthday;
}
