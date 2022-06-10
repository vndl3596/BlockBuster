package com.example.demo.model;

import com.example.demo.model.Key.FkDirectorKey;
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
@Table(name = "fk_director")
public class FKDirector {
    @EmbeddedId
    private FkDirectorKey id = new FkDirectorKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private MovieDetail movieDetail = new MovieDetail();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dricetorId")
    @JoinColumn(name = "dricetor_id")
    private MovieDirector movieDirector = new MovieDirector();
}
