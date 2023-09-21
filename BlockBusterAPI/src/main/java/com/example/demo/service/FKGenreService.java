package com.example.demo.service;

import com.example.demo.dto.FKGenreDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieGenreDTO;

import java.util.List;

public interface FKGenreService {
    List<FKGenreDTO> getAllFKGenre();
    //get all genre on a movie
    List<MovieGenreDTO> getGenreOnMovie(int movieId);

    //get all movie on a genre
    List<MovieDetailDTO> getMovieOnGenre(int genreId);
    List<MovieDetailDTO> getComingMovieOnGenre(int genreId);
    List<MovieDetailDTO> getShowingMovieOnGenre(int genreId);
    String addGenreToMovie(String[] genres, int movieId);

    void deleteByGenreId(int genreId);

    void deleteByMovieId(int movieId);

    void removeGenreExits(Integer movieId);
}
