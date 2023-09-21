package com.example.demo.model;

import com.example.demo.model.Key.FkGenreKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fk_genre")
public class FKGenre {
    @EmbeddedId
    private FkGenreKey id = new FkGenreKey();


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private MovieDetail movieDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private MovieGenre movieGenre;
}
