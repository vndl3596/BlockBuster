package com.example.demo.service;

import com.example.demo.DTO.CastPage;
import com.example.demo.DTO.MovieCastDTO;

import java.util.List;

public interface MovieCastService {

    List<MovieCastDTO> getAllCast();

    MovieCastDTO getMovieCastById(int id);

    String deleteMovieCastById(int id);

    MovieCastDTO createMovieCast(MovieCastDTO movieCastDTO);

    String editMovieCast(MovieCastDTO movieCastDTO);

    CastPage getAllCastPage(int pageNo, int pageSize, String sortBy, String sortDir);
}
