package com.example.demo.service.IMPL;

import com.example.demo.dto.FKMembershipDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieDirectorDTO;
import com.example.demo.map.FKMembershipMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.map.MovieDirectorMap;
import com.example.demo.model.FKDirector;
import com.example.demo.model.FKMembership;
import com.example.demo.model.Key.FkDirectorKey;
import com.example.demo.repository.*;
import com.example.demo.service.FKDirectorService;
import com.example.demo.service.FKMembershipService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FKMembershipServiceImpl implements FKMembershipService {
    private final FKMembershipRepository fkMembershipRepository;
    private final FKMembershipMap fkMembershipMap;
    @Override
    public List<FKMembershipDTO> getFkMembershipByUsername(String username){
        List<FKMembership> fkMembershipList = fkMembershipRepository.findAll();
        List<FKMembershipDTO> returnList = new ArrayList<>();

        for (FKMembership fkMembership: fkMembershipList) {
            if(fkMembership.getAccount().getUsername().equals(username) && fkMembership.getToTime().compareTo(new Date()) > 0){
                returnList.add(fkMembershipMap.fkMembershipToDTO(fkMembership));
            }
        }
        return returnList;
    }
}
