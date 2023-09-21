package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_director")
public class MovieDirector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "avatar")
    @NotBlank(message = "Director's avatar cannot be empty")
    private String avatar;

    @Column(name = "name")
    @NotBlank(message = "Director's name cannot be empty")
    private String name;

    @Column(name = "story")
    private String story;

    @Column(name = "birthday")
    @NotNull(message = "Director's birthday cannot be empty")
    private Date birthday;

    @OneToMany(mappedBy = "movieDirector", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<FKDirector> fkDirectors = new ArrayList<>();
}
