package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "membership_buy_history")
public class MembershipBuyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "buy_time")
    @NotBlank(message = "Buy time cannot be empty")
    private Date buyTime;

    @Column(name = "money_income")
    @NotBlank(message = "Money income cannot be empty")
    private Integer moneyIncome;

    @Column(name = "id_voucher")
    private Integer voucherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_membership_detail")
    @EqualsAndHashCode.Exclude
    private MembershipDetail membershipDetailBuy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    @EqualsAndHashCode.Exclude
    private Account account;
}
