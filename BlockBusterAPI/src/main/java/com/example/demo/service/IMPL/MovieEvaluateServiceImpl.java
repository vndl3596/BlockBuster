package com.example.demo.service.IMPL;

import com.example.demo.dto.MovieEvaluateDTO;
import com.example.demo.map.MovieEvaluateMap;
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
    private final MovieEvaluateMap movieEvaluateMap;


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

    @Override
    public MovieEvaluateDTO disableEvaluate(int mvId, int accId){
        MovieEvaluateKey key = new MovieEvaluateKey(accId, mvId);
        MovieEvaluate me = movieEvaluateRepository.getById(key);
        me.setEvaluateContent("Nội dung bị ẩn vì chứa từ ngữ vi phạm chuẩn mực!!!");
        movieEvaluateRepository.save(me);
        return movieEvaluateMap.movieEvaluateToDTO(me);
    }
}
