package com.example.demo.service.IMPL;

import com.example.demo.DTO.FKCastDTO;
import com.example.demo.DTO.MovieCastDTO;
import com.example.demo.DTO.MovieDetailDTO;
import com.example.demo.map.FKCastMap;
import com.example.demo.map.MovieCastMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.model.FKCast;
import com.example.demo.repository.FKCastRepository;
import com.example.demo.service.FKCastService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKCastServiceImpl implements FKCastService {
    private final FKCastRepository fkCastRepository;
    private final FKCastMap fkCastMap;
    private final MovieCastMap movieCastMap;
    private final MovieDetailMap movieDetailMap;

    @Override
    public List<MovieCastDTO> getCastByMovieId(int id) {
        List<MovieCastDTO> movieCastDTOS = new ArrayList<>();
        List<FKCast> fkCasts = fkCastRepository.findAll();
        fkCasts.forEach(fkCast -> {
            if (fkCast.getMovieDetail().getId() == id) {
                movieCastDTOS.add(movieCastMap.movieCastDTO(fkCast.getMovieCast()));
            }
        });
        return movieCastDTOS;
    }

    @Override
    public List<MovieDetailDTO> getMovieDetailByCastId(int id) {
        List<MovieDetailDTO> movieDetailDTOS = new ArrayList<>();
        List<FKCast> fkCasts = fkCastRepository.findAll();
        fkCasts.forEach(fkCast -> {
            if (fkCast.getMovieCast().getId() == id) {
                movieDetailDTOS.add(movieDetailMap.movieDetailToDTO(fkCast.getMovieDetail()));
            }
        });
        return movieDetailDTOS;
    }

    @Override
    public void deleteFkCastByCastId(int castId) {
        List<FKCast> fkCasts = fkCastRepository.findAll();
        fkCasts.forEach(fkCast -> {
            if (fkCast.getMovieCast().getId() == castId) {
                fkCastRepository.delete(fkCast);
            }
        });
    }

    @Override
    public void deleteFkCastByMovieId(int movieDetailId) {
        List<FKCast> fkCasts = fkCastRepository.findAll();
        fkCasts.forEach(fkCast -> {
            if (fkCast.getMovieDetail().getId() == movieDetailId) {
                fkCastRepository.delete(fkCast);
            }
        });
    }

    @Override
    public List<FKCastDTO> getAllFKCast() {
        return fkCastMap.fkCastDTOList(fkCastRepository.findAll());
    }
}
