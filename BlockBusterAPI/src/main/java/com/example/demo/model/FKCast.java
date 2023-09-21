package com.example.demo.model;

import com.example.demo.model.Key.FkCastKey;
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
@Table(name = "fk_cast")
public class FKCast {
    @EmbeddedId
    private FkCastKey id = new FkCastKey();

    @Column(name = "cast_character")
    private String castCharacter;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private MovieDetail movieDetail = new MovieDetail();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("castId")
    @JoinColumn(name = "cast_id")
    private MovieCast movieCast = new MovieCast();
}
