package com.example.demo.service.IMPL;

import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.map.MembershipBuyHistoryMap;
import com.example.demo.map.MembershipDetailMap;
import com.example.demo.model.Account;
import com.example.demo.model.MembershipBuyHistory;
import com.example.demo.model.MembershipDetail;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FKMembershipRepository;
import com.example.demo.repository.MembershipBuyHistoryRepository;
import com.example.demo.repository.MembershipDetailRepository;
import com.example.demo.service.MembershipBuyHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipBuyHistoryServiceImpl implements MembershipBuyHistoryService {
    private final MembershipDetailRepository membershipDetailRepository;
    private final FKMembershipRepository fkMembershipRepository;
    private final MembershipBuyHistoryRepository membershipBuyHistoryRepository;
    private final MembershipBuyHistoryMap membershipBuyHistoryMap;
    private final AccountRepository accountRepository;
    private final MembershipDetailMap membershipDetailMap;

    @Transactional
    @Override
    public String saveBuyHistory(int idAcc, int idMembershipDetail, int voucherId){
        MembershipDetail memberDetail = membershipDetailRepository.getById(idMembershipDetail);
        Account acc = accountRepository.getById(idAcc);

        MembershipBuyHistory membershipBuyHistory = new MembershipBuyHistory();
        membershipBuyHistory.setMembershipDetailBuy(memberDetail);
        membershipBuyHistory.setAccount(acc);
        membershipBuyHistory.setBuyTime(new Date());
        membershipBuyHistory.setMoneyIncome(memberDetail.getPrice());
        membershipBuyHistory.setVoucherId(null);
        membershipBuyHistory = membershipBuyHistoryRepository.saveAndFlush(membershipBuyHistory);
        return "Success";
    }

    @Override
    public List<MembershipBuyHistoryDTO> getAllBuyHistory(){
        return membershipBuyHistoryMap.listMembershipBuyHistoryToListDTO(membershipBuyHistoryRepository.findAll());
    }
}
