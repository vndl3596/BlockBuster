package com.example.demo.model.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FkDirectorKey implements Serializable {

    @Column(name = "director_id")
    int directorId;

    @Column(name = "movie_id")
    int movieId;
}
