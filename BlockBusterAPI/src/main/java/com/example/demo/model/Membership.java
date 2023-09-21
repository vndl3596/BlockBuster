package com.example.demo.model;

import com.example.demo.model.address.Town;
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
@Table(name = "membership")
public class Membership implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "membership_name")
    @NotBlank(message = "Membership's name cannot be empty")
    private String name;

    @Column(name = "membership_detail")
    @NotBlank(message = "Membership's detail cannot be empty")
    private String detail;

    @OneToMany(mappedBy = "requireMember", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<MovieDetail> listMovies = new ArrayList<>();

    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<MembershipDetail> listMembershipDetail = new ArrayList<>();
}
