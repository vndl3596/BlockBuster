package com.example.demo.map;

import com.example.demo.dto.MembershipBuyHistoryDTO;
import com.example.demo.dto.MembershipDetailDTO;
import com.example.demo.model.MembershipBuyHistory;
import com.example.demo.model.MembershipDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipBuyHistoryMap {

    private AccountMap accountMap;
    private MembershipDetailMap membershipDetailMap;

    public MembershipBuyHistoryDTO membershipBuyHistoryToDTO(MembershipBuyHistory membershipBuyHistory) {
        return new MembershipBuyHistoryDTO(membershipBuyHistory.getId(),
                membershipBuyHistory.getBuyTime(), membershipBuyHistory.getMoneyIncome(),
                membershipBuyHistory.getVoucherId(), membershipDetailMap.membershipDetailToDTO(membershipBuyHistory.getMembershipDetailBuy())
                , accountMap.accountToDTO(membershipBuyHistory.getAccount()));
    }

    public List<MembershipBuyHistoryDTO> listMembershipBuyHistoryToListDTO(List<MembershipBuyHistory> membershipBuyHistories) {
        List<MembershipBuyHistoryDTO> membershipDetailDTOs = new ArrayList<>();
        membershipBuyHistories.forEach(membershipBuyHistory -> {
            membershipDetailDTOs.add(this.membershipBuyHistoryToDTO(membershipBuyHistory));
        });
        return membershipDetailDTOs;
    }
}
