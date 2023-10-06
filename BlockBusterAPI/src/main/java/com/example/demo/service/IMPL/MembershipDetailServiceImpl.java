package com.example.demo.service.IMPL;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.map.MembershipDetailMap;
import com.example.demo.map.MembershipMap;
import com.example.demo.map.MovieCastMap;
import com.example.demo.model.Account;
import com.example.demo.model.FKMembership;
import com.example.demo.model.Key.FkAccountMemberKey;
import com.example.demo.model.MembershipBuyHistory;
import com.example.demo.model.MembershipDetail;
import com.example.demo.repository.*;
import com.example.demo.service.MembershipDetailService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipDetailServiceImpl implements MembershipDetailService{
    private final MembershipDetailRepository membershipDetailRepository;
    private final FKMembershipRepository fkMembershipRepository;
    private final AccountRepository accountRepository;

    @Override
    public String buyMembership(int idAcc, int idMembershipDetail){
        MembershipDetail memberDetail = membershipDetailRepository.getById(idMembershipDetail);
        Account acc = accountRepository.getById(idAcc);

        FkAccountMemberKey fkAccountMemberKey = new FkAccountMemberKey();
        fkAccountMemberKey.setAccountId(idAcc);
        fkAccountMemberKey.setMembershipId(memberDetail.getMembership().getId());

       FKMembership fkMembership;
        if(!fkMembershipRepository.existsById(fkAccountMemberKey)){
            fkMembership = new FKMembership();
            fkMembership.setId(fkAccountMemberKey);
            fkMembership.setMembership(memberDetail.getMembership());
            fkMembership.setAccount(acc);
            fkMembership.setToTime(new Date());
            fkMembership.setToTime(DateUtils.addDays(fkMembership.getToTime(), memberDetail.getDay()));
            fkMembership.setToTime(DateUtils.addMonths(fkMembership.getToTime(), memberDetail.getMonth()));
            fkMembership.setToTime(DateUtils.addYears(fkMembership.getToTime(), memberDetail.getYear()));
        }
        else{
            fkMembership = fkMembershipRepository.getById(fkAccountMemberKey);
            fkMembership.setToTime(DateUtils.addDays(fkMembership.getToTime(), memberDetail.getDay()));
            fkMembership.setToTime(DateUtils.addMonths(fkMembership.getToTime(), memberDetail.getMonth()));
            fkMembership.setToTime(DateUtils.addYears(fkMembership.getToTime(), memberDetail.getYear()));
        }
        fkMembershipRepository.save(fkMembership);
        return "Success";
    }
}
