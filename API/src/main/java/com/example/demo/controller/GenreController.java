package com.example.demo.controller;

import com.example.demo.DTO.MovieGenreDTO;
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


    //don't need page genre
    //get all genre
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
    @PostMapping("/create")
    public ResponseEntity<MovieGenreDTO> createGenre(@RequestBody MovieGenreDTO movieGenreDTO) {
        return new ResponseEntity<>(movieGenreService.createMovieGenre(movieGenreDTO), HttpStatus.OK);
    }

    //Edit genre
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editGenre(@PathVariable MovieGenreDTO id) {
        return new ResponseEntity<>(movieGenreService.editMovieGenre(id), HttpStatus.OK);
    }

    //Delete genre by id
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteGenreById(@PathVariable int id) {
        return new ResponseEntity<>(movieGenreService.deleteMovieGenreById(id), HttpStatus.OK);
    }
}
