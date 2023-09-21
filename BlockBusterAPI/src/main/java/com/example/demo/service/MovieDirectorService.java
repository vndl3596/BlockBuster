package com.example.demo.service;

import com.example.demo.dto.DirectorPage;
import com.example.demo.dto.MovieDirectorDTO;

import java.util.List;

public interface MovieDirectorService {
    List<MovieDirectorDTO> getAllMovieDirector();

    MovieDirectorDTO getMovieDirectorById(int id);

    String deleteMovieDirectorById(int id);

    MovieDirectorDTO createMovieDirector(MovieDirectorDTO movieDirectorDTO);

    MovieDirectorDTO editMovieDirector(MovieDirectorDTO movieDirectorDTO);

    DirectorPage getAllDirectorPage(int pageNo, int pageSize, String sortBy, String sortDir);

}
