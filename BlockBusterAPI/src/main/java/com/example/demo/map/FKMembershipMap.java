package com.example.demo.map;

import com.example.demo.dto.FKGenreDTO;
import com.example.demo.dto.FKMembershipDTO;
import com.example.demo.model.FKGenre;
import com.example.demo.model.FKMembership;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKMembershipMap {

    public FKMembershipDTO fkMembershipToDTO(FKMembership fkMembership) {
        return new FKMembershipDTO(fkMembership.getMembership().getId(), fkMembership.getAccount().getId(), fkMembership.getToTime());
    }

    public List<FKMembershipDTO> listFKMembershipToListDTO(List<FKMembership> fkGenres) {
        List<FKMembershipDTO> fkMembershipDTOS = new ArrayList<>();
        fkGenres.forEach(account -> {
            fkMembershipDTOS.add(this.fkMembershipToDTO(account));
        });
        return fkMembershipDTOS;
    }
}
