package com.example.demo.map;


import com.example.demo.dto.VoucherDTO;
import com.example.demo.model.Voucher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VoucherMap {

    public List<VoucherDTO> listVoucherToListVoucherDTO(List<Voucher> list) {
        return list.stream().map(this::voucherDTO).toList();
    }

    public VoucherDTO voucherDTO(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setId(voucher.getId());
        voucherDTO.setVoucherCode(voucher.getVoucherCode());
        voucherDTO.setVoucherName(voucher.getVoucherName());
        voucherDTO.setTimeFrom(voucher.getTimeFrom());
        voucherDTO.setTimeTo(voucher.getTimeTo());
        voucherDTO.setStatus(voucher.getStatus());
        voucherDTO.setReduce(voucher.getReduce());
        voucherDTO.setType(voucher.getType());
        return voucherDTO;
    }

    public Voucher voucher(VoucherDTO voucherDTO) {
        Voucher voucher = new Voucher();
        voucher.setId(voucherDTO.getId());
        voucher.setVoucherCode(voucherDTO.getVoucherCode());
        voucher.setVoucherName(voucherDTO.getVoucherName());
        voucher.setTimeFrom(voucherDTO.getTimeFrom());
        voucher.setTimeTo(voucherDTO.getTimeTo());
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setReduce(voucherDTO.getReduce());
        voucher.setType(voucherDTO.getType());
        return voucher;
    }
}
