package com.example.demo.model;

import com.example.demo.model.address.City;
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
@Table(name = "membership_detail")
public class MembershipDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "year")
    @NotNull(message = "Year cannot be empty")
    private Integer year;

    @Column(name = "month")
    @NotNull(message = "Month cannot be empty")
    private Integer month;

    @Column(name = "day")
    @NotNull(message = "Day cannot be empty")
    private Integer day;

    @Column(name = "price")
    @NotNull(message = "Price cannot be empty")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    @EqualsAndHashCode.Exclude
    private Membership membership;

    @OneToMany(mappedBy = "membershipDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<Voucher> voucherList = new ArrayList<>();

    @OneToMany(mappedBy = "membershipDetailBuy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<MembershipBuyHistory> membershipBuyHistories = new ArrayList<>();
}
