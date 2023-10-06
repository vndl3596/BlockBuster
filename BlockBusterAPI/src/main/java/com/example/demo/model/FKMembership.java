package com.example.demo.model;

import com.example.demo.model.Key.FkAccountMemberKey;
import com.example.demo.model.Key.FkGenreKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fk_account_membership")
public class FKMembership {
    @EmbeddedId
    private FkAccountMemberKey id = new FkAccountMemberKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("membershipId")
    @JoinColumn(name = "membership_id")
    private Membership membership;

    @JoinColumn(name = "to_time")
    private Date toTime;
}
