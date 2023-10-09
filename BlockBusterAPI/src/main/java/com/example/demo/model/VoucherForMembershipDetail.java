package com.example.demo.model;

import com.example.demo.model.Key.RoleForAccountKey;
import com.example.demo.model.Key.VoucherForMembershipDetailKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voucher_for_membership_detail")
public class VoucherForMembershipDetail {
    @EmbeddedId
    private VoucherForMembershipDetailKey id = new VoucherForMembershipDetailKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("membershipDetailId")
    @JoinColumn(name = "id_membership_detail")
    private MembershipDetail membershipDetail = new MembershipDetail();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("voucherId")
    @JoinColumn(name = "id_voucher")
    private Voucher voucher = new Voucher();
}