package com.example.demo.service;

import com.example.demo.dto.VoucherDTO;

import java.util.List;

public interface VoucherService {
    List<VoucherDTO> getAllVouchers();

    VoucherDTO createVoucher(VoucherDTO voucherDTO);

    VoucherDTO editVoucher(VoucherDTO voucherDTO);

    String deleteVoucher(int id);

    VoucherDTO getVoucherById(int id);

    VoucherDTO getVoucherByName(String name);

    VoucherDTO getVoucherByCode(String code);


}
