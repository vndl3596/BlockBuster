package com.example.blockbuster.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FKDirectorDTO {
    private MovieDTO idMovie;
    private DirectorDTO idDirector;
}
