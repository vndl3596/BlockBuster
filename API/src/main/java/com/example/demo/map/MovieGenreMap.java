package com.example.demo.map;

import com.example.demo.DTO.MovieGenreDTO;
import com.example.demo.model.MovieGenre;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieGenreMap {
    public MovieGenreDTO movieGenreToDTO(MovieGenre movieGenre) {
        return new MovieGenreDTO(movieGenre.getId(), movieGenre.getName());
    }

    public List<MovieGenreDTO> listMovieGenreToDTO(List<MovieGenre> movieGenres) {
        List<MovieGenreDTO> movieGenreDTOS = new ArrayList<>();
        movieGenres.forEach(movieGenre -> {
            movieGenreDTOS.add(movieGenreToDTO(movieGenre));
        });
        return movieGenreDTOS;
    }

    public MovieGenre DTOToMovieGenre(MovieGenreDTO movieGenreDTO) {
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setId(movieGenreDTO.getId());
        movieGenre.setName(movieGenre.getName());
        return movieGenre;
    }
}
