package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.MovieDetail;

import java.util.List;

public interface MovieDetailService {
    List<MovieDetailDTO> getAllMovie();
    List<MovieDetailDTO> getAllShowingMovie();
    List<MovieDetailDTO> getAllCommingMovie();
    MovieDetailDTO getMovieById(int movieId);

    MovieDetailPage getAllMovieDetailPage(int pageNo, int pageSize, String sortBy, String sortDir);

    MovieDetailDTO addMovieDetail(MovieDetailDTO movieDetailDTO) throws Exception;

    MovieDetailDTO editMovieDetail(MovieDetailDTO movieDetailDTO) throws Exception;

    String deleteMovieDetail(int id) throws Exception;

    MovieDetail getMovieDetailByTitle(String title);

    List<MovieRate> getListMovieRate() throws Exception;

    MovieRate getRateMovie(int id) throws Exception;

    List<MovieGenreDTO> getMovieGenres(int id);


    List<MovieCastDTO> getMovieCasts(int id);

    List<MovieDetailDTO> search(String searchQuery);

    List<MovieEvaluateDTO> loadEvaluate(int movieId);

    List<MovieEvaluateDTO> loadEvaluateInAcc(int accId);

    MovieEvaluateDTO saveEvaluate(MovieEvaluateDTO movieEvaluateDTO);

    int addView(int id);
}
