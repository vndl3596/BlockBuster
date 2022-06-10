package com.example.demo.service;

import com.example.demo.DTO.*;
import com.example.demo.model.MovieDetail;

import java.util.List;

public interface MovieDetailService {
    List<MovieDetailDTO> getAllMovie();

    MovieDetailDTO getMovieById(int movieId);

    MovieDetailPage getAllMovieDetailPage(int pageNo, int pageSize, String sortBy, String sortDir);

    MovieDetailDTO addMovieDetail(MovieDetail movieDetailDTO) throws Exception;

    MovieDetail editMovieDetail(MovieDetail movieDetailDTO) throws Exception;

    MovieDetailDTO editMovie(MovieDetailDTO movieDetailDTO) throws Exception;

    MovieDetail deleteMovieDetail(int id) throws Exception;

    MovieDetail getMovieDetailByTitle(String title);

    List<MovieRate> getListMovieRate();

    float getRateMovie(int id);

    List<MovieGenreDTO> getMovieGenres(int id);


    List<MovieCastDTO> getMovieCasts(int id);

    List<MovieDetailDTO> search(String searchQuery);

    List<MovieEvaluateDTO> loadEvaluate(int movieId);

    List<MovieEvaluateDTO> loadEvaluateInAcc(int accId);

    MovieEvaluateDTO saveEvaluate(MovieEvaluateDTO movieEvaluateDTO);
}
