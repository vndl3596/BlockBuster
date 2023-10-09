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
public class VoucherForMembershipDetailKey implements Serializable {
    @Column(name = "id_membership_detail")
    int membershipDetailId;

    @Column(name = "id_voucher")
    int voucherId;
}