package com.example.demo.service;

import com.example.demo.dto.UserHistoryDTO;
import com.example.demo.dto.VoucherDTO;

import java.text.ParseException;
import java.util.List;

public interface VoucherService {
    List<VoucherDTO> getAllVoucher();
    VoucherDTO getVoucherById(int idVoucher);
    VoucherDTO addVoucher(VoucherDTO voucherDTO);
    VoucherDTO editVoucher(VoucherDTO voucherDTO);
    String deleteVoucherById(int voucherId);
    VoucherDTO getVoucherByMemDeId(int idMemDe);
}
