package com.example.demo.service.IMPL;

import com.example.demo.dto.VoucherDTO;
import com.example.demo.map.VoucherMap;
import com.example.demo.model.Voucher;
import com.example.demo.repository.VoucherRepository;
import com.example.demo.service.VoucherService;
import com.example.demo.util.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMap voucherMap;

    @Override
    public List<VoucherDTO> getAllVouchers() {
        return voucherMap.listVoucherToListVoucherDTO(voucherRepository.findAll());
    }

    @Override
    public VoucherDTO createVoucher(VoucherDTO voucherDTO) {
        Voucher voucherFound = voucherRepository.findVoucherByVoucherCode(voucherDTO.getVoucherCode()).orElse(null);
        if (voucherFound != null && voucherFound.getVoucherCode().equals(voucherDTO.getVoucherCode())) {
            throw new RuntimeException(Message.VOUCHER_CODE_CONFLICT);
        }
        Voucher voucher = new Voucher();
        voucher.setVoucherCode(voucherDTO.getVoucherCode());
        voucher.setVoucherName(voucherDTO.getVoucherName());
        voucher.setTimeFrom(voucherDTO.getTimeFrom());
        voucher.setTimeTo(voucherDTO.getTimeTo());
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setReduce(voucherDTO.getReduce());
        voucher.setType(voucherDTO.getType());
        voucherRepository.save(voucher);
        voucherDTO.setId(voucher.getId());
        return voucherDTO;
    }

    @Override
    public VoucherDTO editVoucher(VoucherDTO voucherDTO) {
        Voucher voucher = voucherRepository.findById(voucherDTO.getId())
                .orElse(null);
        if (voucher == null) {
            throw new RuntimeException(Message.VOUCHER_NOT_FOUND);
        }
        Voucher voucherFound = voucherRepository.findVoucherByVoucherCode(voucherDTO.getVoucherCode()).orElse(null);
        if (voucherFound != null &&
                !voucherFound.getVoucherCode().equals(voucher.getVoucherCode()) &&
                voucherFound.getVoucherCode().equals(voucherDTO.getVoucherCode())) {
            throw new RuntimeException(Message.VOUCHER_CODE_CONFLICT);
        }

        voucher.setVoucherCode(voucherDTO.getVoucherCode());
        voucher.setVoucherName(voucherDTO.getVoucherName());
        voucher.setTimeFrom(voucherDTO.getTimeFrom());
        voucher.setTimeTo(voucherDTO.getTimeTo());
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setReduce(voucherDTO.getReduce());
        voucher.setType(voucherDTO.getType());
        voucherRepository.save(voucher);
        return voucherDTO;
    }

    @Override
    public String deleteVoucher(int id) {
        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher == null) {
            throw new RuntimeException(Message.VOUCHER_NOT_FOUND);
        }
        voucherRepository.deleteById(id);
        return Message.DELETE_VOUCHER_SUCCESS;
    }

    @Override
    public VoucherDTO getVoucherById(int id) {
        Voucher voucher = voucherRepository.findById(id).orElse(null);
        if (voucher == null) {
            throw new RuntimeException(Message.VOUCHER_NOT_FOUND);
        }
        return voucherMap.voucherDTO(voucher);
    }

    @Override
    public VoucherDTO getVoucherByName(String name) {
        Voucher voucher = voucherRepository.findVoucherByVoucherName(name).orElse(null);
        if (voucher == null) {
            throw new RuntimeException(Message.VOUCHER_NOT_FOUND);
        }
        return voucherMap.voucherDTO(voucher);
    }

    @Override
    public VoucherDTO getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findVoucherByVoucherCode(code).orElse(null);
        if (voucher == null) {
            throw new RuntimeException(Message.VOUCHER_NOT_FOUND);
        }
        return voucherMap.voucherDTO(voucher);
    }
}
