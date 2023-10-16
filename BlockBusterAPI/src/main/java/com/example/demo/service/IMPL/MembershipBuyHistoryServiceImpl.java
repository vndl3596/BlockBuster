package com.example.demo.service.IMPL;

import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.map.MembershipBuyHistoryMap;
import com.example.demo.model.Account;
import com.example.demo.model.MembershipBuyHistory;
import com.example.demo.model.MembershipDetail;
import com.example.demo.model.Voucher;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.MembershipBuyHistoryRepository;
import com.example.demo.repository.MembershipDetailRepository;
import com.example.demo.repository.VoucherRepository;
import com.example.demo.service.MembershipBuyHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipBuyHistoryServiceImpl implements MembershipBuyHistoryService {
    private final MembershipDetailRepository membershipDetailRepository;
    private final MembershipBuyHistoryRepository membershipBuyHistoryRepository;
    private final MembershipBuyHistoryMap membershipBuyHistoryMap;
    private final AccountRepository accountRepository;
    private final VoucherRepository voucherRepository;
    @Override
    public String saveBuyHistory(int idAcc, int idMembershipDetail, int voucherId) throws Exception {
        try {
            MembershipDetail memberDetail = membershipDetailRepository.findById(idMembershipDetail).orElse(null);
            Account acc = accountRepository.findById(idAcc).orElse(null);
            Voucher voucher = voucherRepository.findById(voucherId).orElse(null);

            MembershipBuyHistory membershipBuyHistory = new MembershipBuyHistory();
            membershipBuyHistory.setMembershipDetailBuy(memberDetail);
            membershipBuyHistory.setAccountBuy(acc);
            membershipBuyHistory.setBuyTime(new Date());
            if(voucher != null){
                membershipBuyHistory.setVoucher(voucher);
                if(voucher.getType() == 1){
                    membershipBuyHistory.setMoneyIncome(memberDetail.getPrice() - (memberDetail.getPrice() * voucher.getReduce() / 100));
                }
                else{
                    membershipBuyHistory.setMoneyIncome(memberDetail.getPrice() - voucher.getReduce());
                }
            }
            else {
                membershipBuyHistory.setMoneyIncome(memberDetail.getPrice());
            }
            membershipBuyHistoryRepository.save(membershipBuyHistory);
            return "Success";

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<MembershipBuyHistoryDTO> getAllBuyHistory() throws Exception {
        return membershipBuyHistoryMap.listMembershipBuyHistoryToListDTO(membershipBuyHistoryRepository.findAll());
    }

    @Override
    public List<MembershipBuyHistoryDTO> getAllBuyHistoryByUsername(String username) {
        List<MembershipBuyHistory> membershipBuyHistoryList = membershipBuyHistoryRepository.findAll();
        List<MembershipBuyHistoryDTO> returnList = new ArrayList<>();
        for (MembershipBuyHistory membershipBuyHistory : membershipBuyHistoryList) {
            if (membershipBuyHistory.getAccountBuy().getUsername().equals(username)) {
                returnList.add(membershipBuyHistoryMap.membershipBuyHistoryToDTO(membershipBuyHistory));
            }

        }
        return returnList;
    }
}
