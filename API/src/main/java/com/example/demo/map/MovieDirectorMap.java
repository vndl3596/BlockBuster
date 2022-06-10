package com.example.demo.map;

import com.example.demo.DTO.MovieDirectorDTO;
import com.example.demo.model.MovieDirector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieDirectorMap {
    public MovieDirectorDTO movieDirectorToDTO(MovieDirector movieDirector) {
        return new MovieDirectorDTO(
                movieDirector.getId(),
                movieDirector.getAvatar(),
                movieDirector.getName(),
                movieDirector.getStory(),
                movieDirector.getBirthday()
        );
    }

    public List<MovieDirectorDTO> listMovieDirectorToDTO(List<MovieDirector> movieDirectors) {
        List<MovieDirectorDTO> movieDirectorDTOS = new ArrayList<>();
        movieDirectors.forEach(movieDirector -> {
            movieDirectorDTOS.add(movieDirectorToDTO(movieDirector));
        });
        return movieDirectorDTOS;
    }
}
