package com.example.demo.service;

import com.example.demo.dto.MembershipDetailDTO;
import com.example.demo.model.MembershipDetail;

import java.util.List;

public interface MembershipDetailService {
    String buyMembership(int idAcc, int idMembershipDetail);
    MembershipDetailDTO addMembershipDetail(MembershipDetailDTO membershipDetailDTO);
    String deleteMembershipDetailById(int membershipDetailId);
    MembershipDetailDTO editMembershipDetail(MembershipDetailDTO membershipDetailDTO);
    List<MembershipDetailDTO> getMembershipDetailByMembershipId(int idMembership);
}
