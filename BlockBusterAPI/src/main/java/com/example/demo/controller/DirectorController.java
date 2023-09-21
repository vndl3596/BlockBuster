package com.example.demo.controller;

import com.example.demo.dto.DirectorPage;
import com.example.demo.dto.MovieCastDTO;
import com.example.demo.dto.MovieDirectorDTO;
import com.example.demo.service.MovieDirectorService;
import com.example.demo.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/director")
public class DirectorController {
    private final MovieDirectorService movieDirectorService;

    //get all director
    @GetMapping("/getAll")
    public ResponseEntity<List<MovieDirectorDTO>> getAllDirectors() {
        return new ResponseEntity<>(movieDirectorService.getAllMovieDirector(), HttpStatus.OK);
    }
    //get page directors
    @GetMapping("/{id}")
    public ResponseEntity<MovieDirectorDTO> getDirectorById(@PathVariable int id) {
        return new ResponseEntity<>(movieDirectorService.getMovieDirectorById(id), HttpStatus.OK);
    }

    //create a director
    @PostMapping("/create")
    public ResponseEntity<MovieDirectorDTO> createDirector(@RequestBody MovieDirectorDTO movieDirectorDTO) {
        return new ResponseEntity<>(movieDirectorService.createMovieDirector(movieDirectorDTO), HttpStatus.OK);
    }

    //Edit a director
    @PostMapping("/edit")
    public ResponseEntity<MovieDirectorDTO> editDirector(@RequestBody MovieDirectorDTO movieDirectorDTO) {
        return new ResponseEntity<>(movieDirectorService.editMovieDirector(movieDirectorDTO), HttpStatus.OK);
    }

    //Delete a director
    @GetMapping("/remove/{id}")
    public ResponseEntity<String> deleteDirector(@PathVariable int id) {
        return new ResponseEntity<>(movieDirectorService.deleteMovieDirectorById(id), HttpStatus.OK);
    }
}
