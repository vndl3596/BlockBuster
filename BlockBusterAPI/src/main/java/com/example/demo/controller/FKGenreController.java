package com.example.demo.controller;

import com.example.demo.dto.FKGenreDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieGenreDTO;
import com.example.demo.service.FKGenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/fkGenre")
public class FKGenreController {
    private final FKGenreService fkGenreService;
    @GetMapping("/getAllFKGenre")
    public ResponseEntity<List<FKGenreDTO>> getAllFKGenre(){
        return new ResponseEntity<>(fkGenreService.getAllFKGenre(), HttpStatus.OK);
    }

    //get all genre on a movie
    @GetMapping("/getAllGenre/{movieId}")
    public ResponseEntity<List<MovieGenreDTO>> getAllGenreByMovie(@PathVariable int movieId){
        return new ResponseEntity<>(fkGenreService.getGenreOnMovie(movieId), HttpStatus.OK);
    }

    //get all movie on genre
    @GetMapping("/getAllMovie/{genreId}")
    public ResponseEntity<List<MovieDetailDTO>> getAllMovieByGenre(@PathVariable int genreId){
        return new ResponseEntity<>(fkGenreService.getMovieOnGenre(genreId), HttpStatus.OK);
    }

    @GetMapping("/getAllComingMovie/{genreId}")
    public ResponseEntity<List<MovieDetailDTO>> getAllComingMovieByGenre(@PathVariable int genreId){
        return new ResponseEntity<>(fkGenreService.getComingMovieOnGenre(genreId), HttpStatus.OK);
    }

    @GetMapping("/getAllShowingMovie/{genreId}")
    public ResponseEntity<List<MovieDetailDTO>> getAllShowingMovieByGenre(@PathVariable int genreId){
        return new ResponseEntity<>(fkGenreService.getShowingMovieOnGenre(genreId), HttpStatus.OK);
    }

    @PostMapping("/addGenreForMovie/{movieId}")
    public ResponseEntity<String> addGenreForMovie(@RequestBody String[] genres, @PathVariable int movieId){
        return new ResponseEntity<>(fkGenreService.addGenreToMovie(genres, movieId), HttpStatus.OK);
    }

}
