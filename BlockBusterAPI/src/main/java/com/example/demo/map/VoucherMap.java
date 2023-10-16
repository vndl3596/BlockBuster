package com.example.demo.map;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.dto.VoucherDTO;
import com.example.demo.model.Membership;
import com.example.demo.model.Voucher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class VoucherMap {

    private  final MembershipDetailMap membershipDetailMap;

    public VoucherDTO voucherToDTO(Voucher voucher) {
        return new VoucherDTO(voucher.getId(), voucher.getVoucherCode(), voucher.getVoucherName(), voucher.getTimeFrom(), voucher.getTimeTo(), voucher.getStatus(), voucher.getReduce(), voucher.getType(), membershipDetailMap.membershipDetailToDTO(voucher.getMembershipDetail()));
    }

    public List<VoucherDTO> listVoucherToListDTO(List<Voucher> vouchers) {
        List<VoucherDTO> voucherDTOS = new ArrayList<>();
        vouchers.forEach(voucher -> {
            voucherDTOS.add(this.voucherToDTO(voucher));
        });
        return voucherDTOS;
    }
}
