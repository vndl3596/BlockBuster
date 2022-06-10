package com.example.demo.model.address;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "district")
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_province_city")
    @EqualsAndHashCode.Exclude
    private City city;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<Town> towns;

}
