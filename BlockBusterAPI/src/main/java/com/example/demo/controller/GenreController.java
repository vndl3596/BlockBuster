package com.example.demo.controller;

import com.example.demo.dto.MovieGenreDTO;
import com.example.demo.service.MovieGenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/genre")
public class GenreController {
    private final MovieGenreService movieGenreService;


    @GetMapping("/getAll")
    public ResponseEntity<List<MovieGenreDTO>> getAllGenres() {
        return new ResponseEntity<>(movieGenreService.getAllMovieGen(), HttpStatus.OK);
    }

    //get genre by id
    @GetMapping("/getById/{id}")
    public ResponseEntity<MovieGenreDTO> getGenreById(@PathVariable int id) {
        return new ResponseEntity<>(movieGenreService.getMovieGenreById(id), HttpStatus.OK);
    }

    //Create a genre
    @PostMapping("/add")
    public ResponseEntity<MovieGenreDTO> createGenre(@RequestBody MovieGenreDTO movieGenreDTO) {
        return new ResponseEntity<>(movieGenreService.createMovieGenre(movieGenreDTO), HttpStatus.OK);
    }

    //Edit genre
    @PostMapping("/edit")
    public ResponseEntity<MovieGenreDTO> editGenre(@RequestBody MovieGenreDTO movieGenreDTO) {
        return new ResponseEntity<>(movieGenreService.editMovieGenre(movieGenreDTO), HttpStatus.OK);
    }

    //Delete genre by id
    @GetMapping("/remove/{id}")
    public ResponseEntity<String> deleteGenreById(@PathVariable int id) {
        return new ResponseEntity<>(movieGenreService.deleteMovieGenreById(id), HttpStatus.OK);
    }
}
