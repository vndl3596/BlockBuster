package com.example.demo.service;

import com.example.demo.dto.CastPage;
import com.example.demo.dto.MovieCastDTO;

import java.util.List;

public interface MovieCastService {

    List<MovieCastDTO> getAllCast();

    MovieCastDTO getMovieCastById(int id);

    String deleteMovieCastById(int id);

    MovieCastDTO createMovieCast(MovieCastDTO movieCastDTO);

    MovieCastDTO editMovieCast(MovieCastDTO movieCastDTO);

    CastPage getAllCastPage(int pageNo, int pageSize, String sortBy, String sortDir);
}
