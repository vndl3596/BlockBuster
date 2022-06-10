package com.example.demo.service.IMPL;

import com.example.demo.DTO.MovieEvaluateDTO;
import com.example.demo.model.Key.MovieEvaluateKey;
import com.example.demo.model.MovieEvaluate;
import com.example.demo.repository.MovieEvaluateRepository;
import com.example.demo.service.MovieEvaluateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieEvaluateServiceImpl implements MovieEvaluateService {
    private final MovieEvaluateRepository movieEvaluateRepository;

    //    private final MovieEvaluate
    @Override
    public String addEvaluateMovie(MovieEvaluateDTO movieEvaluateDTO, int accountId, int movieId) {
        MovieEvaluateKey movieEvaluateKey = new MovieEvaluateKey();
        movieEvaluateKey.setMovieId(movieId);
        movieEvaluateKey.setUserId(accountId);
        MovieEvaluate movieEvaluate = new MovieEvaluate();
        movieEvaluate.setId(movieEvaluateKey);
        movieEvaluate.setEvaluateTime(movieEvaluateDTO.getEvaluateTime());
        movieEvaluate.setEvaluateContent(movieEvaluateDTO.getEvaluateContent());
        movieEvaluate.setEvaluateRate(movieEvaluateDTO.getEvaluateRate());
        movieEvaluateRepository.save(movieEvaluate);
        return "Add movieEvaluate successfully";
    }

    @Override
    public List<MovieEvaluate> getMovieEvaluates() {
        return movieEvaluateRepository.findAll();
    }

    @Override
    public void deleteMovieEvaluateByMovieId(int movieId) {
        List<MovieEvaluate> movieEvaluates = movieEvaluateRepository.findAll();
        movieEvaluates.forEach(movieEvaluate -> {
            if (movieEvaluate.getId().getMovieId() == movieId) {
                movieEvaluateRepository.delete(movieEvaluate);
            }
        });
    }

    @Override
    public void deleteMovieEvaluateByUserId(int userId) {
        List<MovieEvaluate> movieEvaluates = movieEvaluateRepository.findAll();
        movieEvaluates.forEach(movieEvaluate -> {
            if (movieEvaluate.getId().getUserId() == userId) {
                movieEvaluateRepository.delete(movieEvaluate);
            }
        });
    }

    @Override
    public void editEvaluate(MovieEvaluate movieEvaluate) {
        movieEvaluateRepository.save(movieEvaluate);
    }
}
