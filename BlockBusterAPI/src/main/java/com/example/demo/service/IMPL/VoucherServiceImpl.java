package com.example.demo.service.IMPL;

import com.example.demo.dto.UserHistoryDTO;
import com.example.demo.dto.VoucherDTO;
import com.example.demo.map.UserHistoryMap;
import com.example.demo.map.VoucherMap;
import com.example.demo.model.MembershipDetail;
import com.example.demo.model.UserHistory;
import com.example.demo.model.Voucher;
import com.example.demo.repository.*;
import com.example.demo.service.UserHistoryService;
import com.example.demo.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherMap voucherMap;
    private final VoucherRepository voucherRepository;
    private final MembershipDetailRepository membershipDetailRepository;
    @Override
    public List<VoucherDTO> getAllVoucher(){
        return voucherMap.listVoucherToListDTO(voucherRepository.findAll());
    }
    @Override
    public VoucherDTO getVoucherById(int idVoucher){
        Voucher voucher = voucherRepository.findById(idVoucher).orElse(null);
        return voucherMap.voucherToDTO(voucher);
    }
    @Override
    public VoucherDTO addVoucher(VoucherDTO voucherDTO){
        Voucher voucher = new Voucher();
        voucher.setVoucherCode(voucherDTO.getVoucherCode());
        voucher.setVoucherName(voucherDTO.getVoucherName());
        voucher.setReduce(voucherDTO.getReduce());
        voucher.setType(voucherDTO.getType());
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setTimeFrom(voucherDTO.getTimeFrom());
        voucher.setTimeTo(voucherDTO.getTimeTo());
        voucher.setMembershipDetail(membershipDetailRepository.findById(voucherDTO.getPackageReduce().getId()).orElse(null));
        voucherRepository.save(voucher);
        voucherDTO.setId(voucher.getId());
        return voucherDTO;
    }

    @Override
    public VoucherDTO editVoucher(VoucherDTO voucherDTO){
        Voucher voucher = voucherRepository.findById(voucherDTO.getId()).orElse(null);
        voucher.setVoucherName(voucherDTO.getVoucherName());
        voucher.setReduce(voucherDTO.getReduce());
        voucher.setType(voucherDTO.getType());
        voucher.setStatus(voucherDTO.getStatus());
        voucher.setTimeFrom(voucherDTO.getTimeFrom());
        voucher.setTimeTo(voucherDTO.getTimeTo());
        voucher.setMembershipDetail(membershipDetailRepository.findById(voucherDTO.getPackageReduce().getId()).orElse(null));
        voucherRepository.save(voucher);
        return voucherDTO;
    }

    @Override
    public String deleteVoucherById(int voucherId){
        Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
        voucherRepository.delete(voucher);
        return "Success";
    }

    @Override
    public VoucherDTO getVoucherByMemDeId(int idMemDe){
        MembershipDetail membershipDetail = membershipDetailRepository.findById(idMemDe).orElse(null);
        int minPrice = membershipDetail.getPrice();
        VoucherDTO voucherDTO = new VoucherDTO();
        for (Voucher voucher: membershipDetail.getVoucherList()) {
            int newPrice;
            if(voucher.getTimeTo().compareTo(new Date()) >= 0){
                if(voucher.getType() == 1){
                    newPrice = membershipDetail.getPrice() - (membershipDetail.getPrice() * voucher.getReduce() / 100);
                }
                else {
                    newPrice = membershipDetail.getPrice() - voucher.getReduce();
                }
                if(newPrice < minPrice){
                    minPrice = newPrice;
                    voucherDTO = voucherMap.voucherToDTO(voucher);
                }
            }
        }
        return voucherDTO;
    }
}
