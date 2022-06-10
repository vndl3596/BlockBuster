package com.example.demo.controller;

import com.example.demo.DTO.FKCastDTO;
import com.example.demo.DTO.MovieCastDTO;
import com.example.demo.DTO.MovieDetailDTO;
import com.example.demo.service.FKCastService;
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
@RequestMapping("/api/fkCast")
public class FKCastController {
    private final FKCastService fkCastService;

    @GetMapping("/getAllFkCast")
    public ResponseEntity<List<FKCastDTO>> getAllFkCast(){
        return new ResponseEntity<>(fkCastService.getAllFKCast(), HttpStatus.OK);
    }

    // get all movie on a cast
    @GetMapping("/movie/{castId}")
    public ResponseEntity<List<MovieDetailDTO>> getAllMovieOnACast(@PathVariable int castId) {
        return new ResponseEntity<>(fkCastService.getMovieDetailByCastId(castId), HttpStatus.OK);
    }

    // get all movie on a cast
    @GetMapping("/cast/{movieId}")
    public ResponseEntity<List<MovieCastDTO>> getAllCastOnAMovie(@PathVariable int movieId) {
        return new ResponseEntity<>(fkCastService.getCastByMovieId(movieId), HttpStatus.OK);
    }

}
