package com.example.demo.service;

import com.example.demo.dto.FKCastDTO;
import com.example.demo.dto.MovieDetailDTO;

import java.util.List;

public interface FKCastService {
    //get all cast on a movie
    List<FKCastDTO> getCastByMovieId(int movieDetailId);

    //get all movie on a cast
    List<MovieDetailDTO> getMovieDetailByCastId(int castId);

    void deleteFkCastByCastId(int castId);

    void deleteFkCastByMovieId(int movieDetailId);

    List<FKCastDTO> getAllFKCast();
    String addActorForMovie(int movieId, int actorId, String character);
    String editActorForMovie(int movieId, int actorId, String character);
    String deleteActorForMovie(int movieId, int actorId);
    void removeFkCastExits(Integer movieId);
}
