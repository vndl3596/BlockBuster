package com.example.demo.service;

import com.example.demo.DTO.MovieEvaluateDTO;
import com.example.demo.model.MovieEvaluate;

import java.util.List;

public interface MovieEvaluateService {
    //add EvaluateMovie
    String addEvaluateMovie(MovieEvaluateDTO movieEvaluateDTO, int accountId, int movieId);

    List<MovieEvaluate> getMovieEvaluates();

    void deleteMovieEvaluateByMovieId(int movieId);

    void deleteMovieEvaluateByUserId(int userId);

    void editEvaluate(MovieEvaluate movieEvaluate);

}
