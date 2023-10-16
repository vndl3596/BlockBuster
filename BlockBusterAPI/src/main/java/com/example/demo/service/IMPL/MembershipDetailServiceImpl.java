package com.example.demo.service.IMPL;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.dto.MembershipDetailDTO;
import com.example.demo.map.MembershipDetailMap;
import com.example.demo.map.MembershipMap;
import com.example.demo.map.MovieCastMap;
import com.example.demo.model.*;
import com.example.demo.model.Key.FkAccountMemberKey;
import com.example.demo.repository.*;
import com.example.demo.service.MembershipDetailService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipDetailServiceImpl implements MembershipDetailService{
    private final MembershipDetailRepository membershipDetailRepository;
    private final MembershipDetailMap membershipDetailMap;
    private final FKMembershipRepository fkMembershipRepository;
    private final MembershipRepository membershipRepository;
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

    @Override
    public MembershipDetailDTO addMembershipDetail(MembershipDetailDTO membershipDetailDTO){
            MembershipDetail membershipDetail = new MembershipDetail();
            membershipDetail.setDay(membershipDetailDTO.getDay());
            membershipDetail.setMonth(membershipDetailDTO.getMonth());
            membershipDetail.setYear(membershipDetailDTO.getYear());
            membershipDetail.setPrice(membershipDetailDTO.getPrice());
            membershipDetail.setMembership(membershipRepository.getById(membershipDetailDTO.getMembership()));
            membershipDetailRepository.save(membershipDetail);
            membershipDetailDTO.setId(membershipDetail.getId());
            return membershipDetailDTO;
    }

    @Override
    public String deleteMembershipDetailById(int membershipDetailId){
        MembershipDetail membershipDetail = membershipDetailRepository.findById(membershipDetailId).orElse(null);
        membershipDetailRepository.delete(membershipDetail);
        return "Success";
    }

    @Override
    public MembershipDetailDTO editMembershipDetail(MembershipDetailDTO membershipDetailDTO){
        MembershipDetail membershipDetail = membershipDetailRepository.findById(membershipDetailDTO.getId()).orElse(null);
        membershipDetail.setYear(membershipDetailDTO.getYear());
        membershipDetail.setMonth(membershipDetailDTO.getMonth());
        membershipDetail.setDay(membershipDetailDTO.getDay());
        membershipDetail.setPrice(membershipDetailDTO.getPrice());
        membershipDetailRepository.save(membershipDetail);
        return membershipDetailDTO;
    }

    @Override
    public List<MembershipDetailDTO> getMembershipDetailByMembershipId(int idMembership){
        List<MembershipDetail> membershipDetails = membershipDetailRepository.findAll();
        List<MembershipDetailDTO> returnList = new ArrayList<>();
        for (MembershipDetail memDe: membershipDetails) {
            if(memDe.getMembership().getId() == idMembership){
                returnList.add(membershipDetailMap.membershipDetailToDTO(memDe));
            }
        }
        return returnList;
    }
}
