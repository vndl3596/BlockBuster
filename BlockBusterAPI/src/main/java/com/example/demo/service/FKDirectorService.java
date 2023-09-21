package com.example.demo.service;

import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieDirectorDTO;

import java.util.List;

public interface FKDirectorService {

    //get all cast on a movie
    List<MovieDirectorDTO> getDirectorByMovieId(int movieDetailId);

    //get all movie on a cast
    List<MovieDetailDTO> getMovieDetailByDirectorId(int directorId);

    void deleteFkDirectorByDirectorId(int directorId);

    void deleteFkDirectorByMovieId(int movieDetailId);

    String addDirectorForMovie(int movieId, int directorId);

    String deleteDirectorForMovie(int movieId, int directorId);

    void removieDirectorExits(Integer movieId);
}
