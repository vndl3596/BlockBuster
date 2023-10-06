package com.example.demo.service.IMPL;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.map.MembershipMap;
import com.example.demo.map.MovieCastMap;
import com.example.demo.model.Membership;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.repository.MovieCastRepository;
import com.example.demo.service.MembershipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MembershipServiceImpl implements MembershipService {
    private final MembershipRepository membershipRepository;
    private final MembershipMap membershipMap;

    @Override
    public List<MembershipDTO> getAllMembershipDetail(){
        List<MembershipDTO> listMember = membershipMap.listMembershipToListDTO(membershipRepository.findAll());
        listMember.remove(0);
        return listMember;
    }

    @Override
    public MembershipDTO getMembershipById(int id){
        return membershipMap.membershipToDTO(membershipRepository.getById(id));
    }

    @Override
    public MembershipDTO addMembership(MembershipDTO membershipDTO){
        Membership membership = new Membership();
        membership.setName(membershipDTO.getName());
        membership.setDetail(membershipDTO.getDetail());
        membershipRepository.save(membership);
        membershipDTO.setId(membership.getId());
        return membershipDTO;
    }

    @Override
    public String deleteMembershipById(int membershipId){
        Membership mem = membershipRepository.getById(membershipId);
        membershipRepository.delete(mem);
        return "Success";
    }

    @Override
    public MembershipDTO editMembership(MembershipDTO membershipDTO){
        Membership mem = membershipRepository.findById(membershipDTO.getId()).orElse(null);
        mem.setName(membershipDTO.getName());
        mem.setDetail(membershipDTO.getDetail());
        membershipRepository.save(mem);
        return membershipDTO;
    }
}
