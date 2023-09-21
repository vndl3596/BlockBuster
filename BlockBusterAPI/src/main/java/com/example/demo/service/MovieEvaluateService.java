package com.example.demo.service;

import com.example.demo.dto.MovieEvaluateDTO;
import com.example.demo.model.MovieEvaluate;

import java.util.List;

public interface MovieEvaluateService {

    List<MovieEvaluate> getMovieEvaluates();

    void deleteMovieEvaluateByMovieId(int movieId);

    void deleteMovieEvaluateByUserId(int userId);

    void editEvaluate(MovieEvaluate movieEvaluate);

    MovieEvaluateDTO disableEvaluate(int mvId, int accId);
}
