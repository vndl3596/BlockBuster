package com.example.demo.service;

import com.example.demo.DTO.MovieGenreDTO;

import java.util.List;

public interface MovieGenreService {
    List<MovieGenreDTO> getAllMovieGen();

    MovieGenreDTO getMovieGenreById(int id);

    String deleteMovieGenreById(int id);

    MovieGenreDTO createMovieGenre(MovieGenreDTO movieGenreDTO);

    String editMovieGenre(MovieGenreDTO movieGenreDTO);
}
