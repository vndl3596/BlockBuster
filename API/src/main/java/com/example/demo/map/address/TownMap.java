package com.example.demo.map.address;

import com.example.demo.DTO.address.TownDTO;
import com.example.demo.model.address.Town;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TownMap {
    private final DistrictMap districtMap;

    public TownDTO townToDTO(Town town) {
        if (town == null) {
            return null;
        } else {
            return new TownDTO(town.getId(), town.getName(), town.getGenre(), town.getDistrict().getId());
        }
    }

    public Town DTOToTown(TownDTO townDTO) {
        Town town = new Town();
        town.setId(townDTO.getId());
        town.setGenre(townDTO.getGenre());
        town.setName(townDTO.getName());
        return town;
    }

    public List<TownDTO> townDTOList(List<Town> towns) {
        List<TownDTO> townDTOS = new ArrayList<>();
        for (Town town : towns) {
            townDTOS.add(townToDTO(town));
        }
        return townDTOS;
    }
}
