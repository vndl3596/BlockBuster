package com.example.demo.map;

import com.example.demo.DTO.FKGenreDTO;
import com.example.demo.model.FKGenre;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKGenreMap {

    public FKGenreDTO fkGenreToDTO(FKGenre fkGenre) {
        return new FKGenreDTO(fkGenre.getId().getMovieId(), fkGenre.getId().getGenreId());
    }

    public List<FKGenreDTO> listGenreToListDTO(List<FKGenre> fkGenres) {
        List<FKGenreDTO> fkGenreDTOS = new ArrayList<>();
        fkGenres.forEach(account -> {
            fkGenreDTOS.add(this.fkGenreToDTO(account));
        });
        return fkGenreDTOS;
    }
}
