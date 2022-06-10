package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "moviedetail")
public class MovieDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    @NotBlank(message = "Movie's title cannot be empty")
    private String title;


    @Column(name = "poster")
    @NotNull(message = "Movie's poster cannot be empty")
    private String poster;

    @Column(name = "details")
    @NotBlank(message = "Movie's details cannot be empty")
    private String detail;


    @Column(name = "movie_status")
    @NotNull(message = "Movie's status cannot be empty")
    private Boolean movieStatus;

    @Column(name = "link_trailer")
    private String linkTrailer;

    @Column(name = "link_movie")
    @NotNull(message = "Movie's link cannot be empty")
    private String linkMovie;

    @Column(name = "release_date")
    @NotNull(message = "Movie' release date cannot be empty")
    @PastOrPresent(message = "Release time is not greater than present")
    private Date releaseDate;

    @Column(name = "time_movie")
    private Time movieDuration;

    @Column(name = "view_num")
    private int viewNumber;

    @OneToMany(mappedBy = "movieDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<MovieEvaluate> movieEvaluates = new ArrayList<>();

    @OneToMany(mappedBy = "movieDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<FKCast> fkCasts = new ArrayList<>();

    @OneToMany(mappedBy = "movieDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<FKGenre> fkGenres = new ArrayList<>();

    @OneToMany(mappedBy = "movieDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<FKDirector> fkDirectors = new ArrayList<>();

}
