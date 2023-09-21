package com.example.demo.controller;

import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.dto.MovieDirectorDTO;
import com.example.demo.service.FKDirectorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/fkDirector")
public class FKDirectorController {
    private final FKDirectorService fkDirectorService;

    //get all movie on a director
    @GetMapping("/movie/{directorId}")
    public ResponseEntity<List<MovieDetailDTO>> getMovieOnDirector(@PathVariable int directorId){
        return new ResponseEntity<>(fkDirectorService.getMovieDetailByDirectorId(directorId), HttpStatus.OK);
    }


    //get all director on a movie
    @GetMapping("/director/{movieId}")
    public ResponseEntity<List<MovieDirectorDTO>> getDirectorOnMovie(@PathVariable int movieId){
        return new ResponseEntity<>(fkDirectorService.getDirectorByMovieId(movieId), HttpStatus.OK);
    }

    @GetMapping("/create/idMv={movieId}&idD={directorId}")
    public ResponseEntity<String> addDirectorOnMovie(@PathVariable int movieId, @PathVariable int directorId){
        return new ResponseEntity<>(fkDirectorService.addDirectorForMovie(movieId, directorId), HttpStatus.OK);
    }

    @GetMapping("/remove/idMv={movieId}&idD={directorId}")
    public ResponseEntity<String> deleteDirectorOnMovie(@PathVariable int movieId, @PathVariable int directorId){
        return new ResponseEntity<>(fkDirectorService.deleteDirectorForMovie(movieId, directorId), HttpStatus.OK);
    }
}
