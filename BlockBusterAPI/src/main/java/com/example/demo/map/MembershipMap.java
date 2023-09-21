package com.example.demo.map;

import com.example.demo.dto.FKGenreDTO;
import com.example.demo.dto.MembershipDTO;
import com.example.demo.model.FKGenre;
import com.example.demo.model.Membership;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MembershipMap {

    public MembershipDTO membershipToDTO(Membership member) {
        return new MembershipDTO(member.getId(), member.getName(), member.getDetail(), new MembershipDetailMap().listMembershipDetailToListDTO(member.getListMembershipDetail()));
    }

    public List<MembershipDTO> listMembershipToListDTO(List<Membership> memberships) {
        List<MembershipDTO> membershipDTOs = new ArrayList<>();
        memberships.forEach(member -> {
            membershipDTOs.add(this.membershipToDTO(member));
        });
        return membershipDTOs;
    }
}
