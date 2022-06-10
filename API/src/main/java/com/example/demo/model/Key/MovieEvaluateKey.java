package com.example.demo.model.Key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEvaluateKey implements Serializable {
    @Column(name = "id_user")
    int userId;

    @Column(name = "id_movie")
    int movieId;
}
