package com.example.demo.service.IMPL;

import com.example.demo.dto.MembershipDTO;
import com.example.demo.map.MembershipMap;
import com.example.demo.map.MovieCastMap;
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
        for (MembershipDTO membership: listMember) {
            if(membership.getId() == 0){
                listMember.remove(membership);
            }
        }
        return listMember;
    }

    @Override
    public MembershipDTO getMembershipById(int id){
        return membershipMap.membershipToDTO(membershipRepository.getById(id));
    }
}
