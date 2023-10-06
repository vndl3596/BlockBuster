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
public class FkAccountMemberKey implements Serializable {

    @Column(name = "account_id")
    int accountId;

    @Column(name = "membership_id")
    int membershipId;
}
