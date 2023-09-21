package com.example.demo.service.IMPL;

import com.example.demo.dto.FKCastDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.map.FKCastMap;
import com.example.demo.map.MovieCastMap;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.model.FKCast;
import com.example.demo.model.FKDirector;
import com.example.demo.model.Key.FkCastKey;
import com.example.demo.model.Key.FkDirectorKey;
import com.example.demo.repository.FKCastRepository;
import com.example.demo.repository.MovieCastRepository;
import com.example.demo.repository.MovieDetailRepository;
import com.example.demo.service.FKCastService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKCastServiceImpl implements FKCastService {
    private final FKCastRepository fkCastRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final MovieCastRepository movieCastRepository;
    private final FKCastMap fkCastMap;
    private final MovieCastMap movieCastMap;
    private final MovieDetailMap movieDetailMap;

    @Override
    public List<FKCastDTO> getCastByMovieId(int id) {
        List<FKCast> fkCasts = fkCastRepository.findAll();
        List<FKCastDTO> fkCastDTOS = new ArrayList<>();
        fkCasts.forEach(fkCast -> {
            if (fkCast.getMovieDetail().getId() == id) {
                fkCastDTOS.add(new FKCastDTO(fkCast.getMovieDetail().getId(), fkCast.getMovieCast().getId(), fkCast.getCastCharacter()));
            }
        });
        return fkCastDTOS;
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

    @Override
    public void removeFkCastExits(Integer movieId) {
        List<FKCast> fkCastsPresent = fkCastRepository.findAll();
        for (FKCast cast : fkCastsPresent) {
            if (cast.getId().getMovieId() == movieId) {
                fkCastRepository.delete(cast);
            }
        }
    }

    @Override
    public String addActorForMovie(int movieId, int actorId, String character){
        for (FKCast fk: fkCastRepository.findAll()) {
            if((fk.getMovieCast().getId() == actorId)&&(fk.getMovieDetail().getId() == movieId)){
                return "success";
            }
        }
        FkCastKey fkKey = new FkCastKey();
        fkKey.setMovieId(movieId);
        fkKey.setCastId(actorId);
        FKCast fkAct = new FKCast();
        fkAct.setId(fkKey);
        fkAct.setMovieCast(movieCastRepository.getById(actorId));
        fkAct.setMovieDetail(movieDetailRepository.getById(movieId));
        fkAct.setCastCharacter(character);
        fkCastRepository.save(fkAct);
        return "success";
    }

    @Override
    public String editActorForMovie(int movieId, int actorId, String character){
        for (FKCast fk: fkCastRepository.findAll()) {
            if((fk.getMovieCast().getId() == actorId)&&(fk.getMovieDetail().getId() == movieId)){
                fk.setCastCharacter(character);
                fkCastRepository.save(fk);
                return "success";
            }
        }
        return "success";
    }
    @Override
    public String deleteActorForMovie(int movieId, int actorId){
        for (FKCast fk: fkCastRepository.findAll()) {
            if((fk.getMovieCast().getId() == actorId)&&(fk.getMovieDetail().getId() == movieId)){
                fkCastRepository.delete(fk);
                return "success";
            }
        }
        return "success";
    }
}
