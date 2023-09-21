package com.example.demo.service.IMPL;

import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieDirectorDTO;
import com.example.demo.map.MovieDetailMap;
import com.example.demo.map.MovieDirectorMap;
import com.example.demo.model.FKDirector;
import com.example.demo.model.Key.FkDirectorKey;
import com.example.demo.repository.FKDirectorRepository;
import com.example.demo.repository.MovieDetailRepository;
import com.example.demo.repository.MovieDirectorRepository;
import com.example.demo.service.FKDirectorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FKDirectorServiceImpl implements FKDirectorService {
    private final FKDirectorRepository fkDirectorRepository;
    private final MovieDetailRepository movieDetailRepository;
    private final MovieDirectorRepository movieDirectorRepository;
    private final MovieDetailMap movieDetailMap;
    private final MovieDirectorMap movieDirectorMap;

    @Override
    public List<MovieDirectorDTO> getDirectorByMovieId(int movieDetailId) {
        List<MovieDirectorDTO> movieDirectorDTOS = new ArrayList<>();
        List<FKDirector> fkDirectors = fkDirectorRepository.findAll();
        fkDirectors.forEach(fkDirector -> {
            if (fkDirector.getMovieDetail().getId() == movieDetailId) {
                movieDirectorDTOS.add(movieDirectorMap.movieDirectorToDTO(fkDirector.getMovieDirector()));
            }
        });
        return movieDirectorDTOS;
    }

    @Override
    public List<MovieDetailDTO> getMovieDetailByDirectorId(int directorId) {
        List<MovieDetailDTO> movieDetailDTOS = new ArrayList<>();
        List<FKDirector> fkDirectors = fkDirectorRepository.findAll();
        fkDirectors.forEach(fkDirector -> {
            if (fkDirector.getMovieDirector().getId() == directorId) {
                movieDetailDTOS.add(movieDetailMap.movieDetailToDTO(fkDirector.getMovieDetail()));
            }
        });
        return movieDetailDTOS;
    }

    @Override
    public void deleteFkDirectorByDirectorId(int directorId) {
        List<FKDirector> fkDirectors = fkDirectorRepository.findAll();
        fkDirectors.forEach(fkDirector -> {
            if (fkDirector.getMovieDirector().getId() == directorId) {
                fkDirectorRepository.delete(fkDirector);
            }
        });
    }

    @Override
    public void deleteFkDirectorByMovieId(int movieDetailId) {
        List<FKDirector> fkDirectors = fkDirectorRepository.findAll();
        fkDirectors.forEach(fkDirector -> {
            if (fkDirector.getMovieDetail().getId() == movieDetailId) {
                fkDirectorRepository.delete(fkDirector);
            }
        });
    }

    @Override
    public void removieDirectorExits(Integer movieId) {
        List<FKDirector> fkDirectorsPresent = fkDirectorRepository.findAll();
        for (FKDirector director : fkDirectorsPresent) {
            if (director.getId().getMovieId() == movieId) {
                fkDirectorRepository.delete(director);
            }
        }
    }

    @Override
    public String addDirectorForMovie(int movieId, int directorId){
        for (FKDirector fk: fkDirectorRepository.findAll()) {
            if((fk.getMovieDirector().getId() == directorId)&&(fk.getMovieDetail().getId() == movieId)){
                return "success";
            }
        }
        FkDirectorKey fkKey = new FkDirectorKey();
        fkKey.setMovieId(movieId);
        fkKey.setDirectorId(directorId);
        FKDirector fkDir = new FKDirector();
        fkDir.setId(fkKey);
        fkDir.setMovieDirector(movieDirectorRepository.getById(directorId));
        fkDir.setMovieDetail(movieDetailRepository.getById(movieId));
        fkDirectorRepository.save(fkDir);
        return "success";
    }

    @Override
    public String deleteDirectorForMovie(int movieId, int directorId){
        for (FKDirector fk: fkDirectorRepository.findAll()) {
            if((fk.getMovieDirector().getId() == directorId)&&(fk.getMovieDetail().getId() == movieId)){
                fkDirectorRepository.delete(fk);
                return "success";
            }
        }
        return "success";
    }
}
