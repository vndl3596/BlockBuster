package com.example.demo.service.IMPL;

import com.example.demo.DTO.MovieGenreDTO;
import com.example.demo.map.MovieGenreMap;
import com.example.demo.model.MovieGenre;
import com.example.demo.repository.MovieGenreRepository;
import com.example.demo.service.FKGenreService;
import com.example.demo.service.MovieGenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieGenreServiceImpl implements MovieGenreService {
    private final MovieGenreRepository movieGenreRepository;
    private final MovieGenreMap movieGenreMap;
    private final FKGenreService fkGenreService;

    @Override
    public List<MovieGenreDTO> getAllMovieGen() {
        return movieGenreMap.listMovieGenreToDTO(movieGenreRepository.findAll());
    }

    @Override
    public MovieGenreDTO getMovieGenreById(int id) {
        return movieGenreMap.movieGenreToDTO(movieGenreRepository.getById(id));
    }

    @Override
    public String deleteMovieGenreById(int id) {
        MovieGenre movieGenre = movieGenreRepository.findById(id).orElse(null);
        if (movieGenre == null) {
            throw new RuntimeException("Not found genre");
        } else {
            fkGenreService.deleteByGenreId(id);
            movieGenreRepository.delete(movieGenre);
            return "Delete genre successfully";
        }
    }

    @Override
    public MovieGenreDTO createMovieGenre(MovieGenreDTO movieGenreDTO) {
        MovieGenre movieGenre = new MovieGenre();
        if (checkGenreName(movieGenreDTO.getName()) == false) {
            movieGenre.setName(movieGenreDTO.getName());
            movieGenreRepository.save(movieGenre);
            return movieGenreDTO;
        }
        return null;
    }

    @Override
    public String editMovieGenre(MovieGenreDTO movieGenreDTO) {
        MovieGenre movieGenre = movieGenreRepository.findById(movieGenreDTO.getId()).orElse(null);
        if (movieGenre == null) {
            throw new RuntimeException("Not found genre");
        } else {
            if (checkGenreName(movieGenreDTO.getName()) == false) {
                movieGenre.setName(movieGenreDTO.getName());
                movieGenreRepository.save(movieGenre);
                return "Edit genre successfully";
            }
        }
        return "Fail";
    }

    public boolean checkGenreName(String name) {
        List<MovieGenre> movieGenres = movieGenreRepository.findAll();
        movieGenres.forEach(movieGenre -> {
            if (movieGenre.getName().equals(name)) {
                throw new RuntimeException("Genre name exit with " + name);
            }
        });
        return false;
    }
}
