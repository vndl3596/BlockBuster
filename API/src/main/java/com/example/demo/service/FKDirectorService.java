package com.example.demo.service;

import com.example.demo.DTO.MovieDetailDTO;
import com.example.demo.DTO.MovieDirectorDTO;

import java.util.List;

public interface FKDirectorService {

    //get all cast on a movie
    List<MovieDirectorDTO> getDirectorByMovieId(int movieDetailId);

    //get all movie on a cast
    List<MovieDetailDTO> getMovieDetailByDirectorId(int directorId);

    void deleteFkDirectorByDirectorId(int directorId);

    void deleteFkDirectorByMovieId(int movieDetailId);
}
