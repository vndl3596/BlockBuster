package com.example.blockbuster.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FKGenreDTO {
    private MovieDTO idMovie;
    private GenreDTO idGenre;
}
