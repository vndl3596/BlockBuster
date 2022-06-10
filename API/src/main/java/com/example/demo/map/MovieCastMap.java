package com.example.demo.map;

import com.example.demo.DTO.MovieCastDTO;
import com.example.demo.model.MovieCast;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieCastMap {
    public MovieCastDTO movieCastDTO(MovieCast movieCast) {
        return new MovieCastDTO(
                movieCast.getId(),
                movieCast.getAvatar(),
                movieCast.getName(),
                movieCast.getStory(),
                movieCast.getBirthday()
        );
    }

    public List<MovieCastDTO> listMovieCastToDTO(List<MovieCast> movieCasts) {
        List<MovieCastDTO> movieCastDTOS = new ArrayList<>();
        movieCasts.forEach(movieCast -> {
            movieCastDTOS.add(movieCastDTO(movieCast));
        });
        return movieCastDTOS;
    }
}
