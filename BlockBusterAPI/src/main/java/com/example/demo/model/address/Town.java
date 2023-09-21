package com.example.demo.model.address;

import com.example.demo.model.Account;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commune_ward_town")
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "genre")
    private String genre;

    @OneToMany(mappedBy = "idTown")
    @EqualsAndHashCode.Exclude
    private List<Account> accounts;

    @ManyToOne
    @JoinColumn(name = "id_district")
    @EqualsAndHashCode.Exclude
    private District district;

}
