package com.example.demo.service;

import com.example.demo.dto.MovieGenreDTO;

import java.util.List;

public interface MovieGenreService {
    List<MovieGenreDTO> getAllMovieGen();

    MovieGenreDTO getMovieGenreById(int id);

    String deleteMovieGenreById(int id);

    MovieGenreDTO createMovieGenre(MovieGenreDTO movieGenreDTO);

    MovieGenreDTO editMovieGenre(MovieGenreDTO movieGenreDTO);
}
