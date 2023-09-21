package com.example.demo.model;

import com.example.demo.model.Key.MovieEvaluateKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "move_evaluate")
public class MovieEvaluate {
    @EmbeddedId
    private MovieEvaluateKey id = new MovieEvaluateKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    private Account accountDetail = new Account();


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "id_movie")
    @EqualsAndHashCode.Exclude
    private MovieDetail movieDetail = new MovieDetail();

    @Column(name = "evaluate_time")
    @PastOrPresent(message = "Evaluate time is not greater than present")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyyd@HH:mm:ss")
    private Date evaluateTime = new Date();

    @Column(name = "evaluate_content")
    private String evaluateContent;

    @Column(name = "evaluate_rate")
    @NotNull(message = "Evaluate rate cannot be null")
    private int evaluateRate;
}
