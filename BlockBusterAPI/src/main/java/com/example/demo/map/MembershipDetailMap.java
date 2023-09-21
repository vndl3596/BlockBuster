package com.example.demo.map;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.dto.MembershipDetailDTO;
import com.example.demo.model.Membership;
import com.example.demo.model.MembershipDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipDetailMap {

    public MembershipDetailDTO membershipDetailToDTO(MembershipDetail memberDetail) {
        return new MembershipDetailDTO(memberDetail.getId(), memberDetail.getYear(), memberDetail.getMonth(), memberDetail.getDay(), memberDetail.getPrice(), memberDetail.getMembership().getId());
    }

    public List<MembershipDetailDTO> listMembershipDetailToListDTO(List<MembershipDetail> membershipDetails) {
        List<MembershipDetailDTO> membershipDetailDTOs = new ArrayList<>();
        membershipDetails.forEach(memberDetail -> {
            membershipDetailDTOs.add(this.membershipDetailToDTO(memberDetail));
        });
        return membershipDetailDTOs;
    }
}
