package com.example.demo.map;

import com.example.demo.DTO.FKCastDTO;
import com.example.demo.model.FKCast;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKCastMap {

    public FKCastDTO fkCastToDTO(FKCast fkCast) {
        return new FKCastDTO(fkCast.getId().getMovieId(), fkCast.getId().getCastId(), fkCast.getCastCharacter());
    }

    public List<FKCastDTO> fkCastDTOList(List<FKCast> fkCasts) {
        List<FKCastDTO> fkCastDTOList = new ArrayList<>();
        for (FKCast fkCast : fkCasts) {
            fkCastDTOList.add(fkCastToDTO(fkCast));
        }
        return fkCastDTOList;
    }

}
