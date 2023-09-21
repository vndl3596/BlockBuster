package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_genre")
public class MovieGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Movie's genre cannot be empty")
    private String name;

    @OneToMany(mappedBy = "movieGenre", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<FKGenre> fkGenres = new ArrayList<>();
}
