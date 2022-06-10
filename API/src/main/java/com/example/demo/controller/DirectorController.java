package com.example.demo.controller;

import com.example.demo.DTO.DirectorPage;
import com.example.demo.DTO.MovieDirectorDTO;
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

    @GetMapping("/page")
    public DirectorPage getAllUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return movieDirectorService.getAllDirectorPage(pageNo, pageSize, sortBy, sortDir);
    }

    //create a director
    @PostMapping("/create")
    public ResponseEntity<MovieDirectorDTO> createDirector(@RequestBody MovieDirectorDTO movieDirectorDTO) {
        System.out.println("\n\n\n\n" + movieDirectorDTO.getBirthday());
        return new ResponseEntity<>(movieDirectorService.createMovieDirector(movieDirectorDTO), HttpStatus.OK);
    }

    //Edit a director
    @PutMapping("/edit")
    public ResponseEntity<String> editDirector(@RequestBody MovieDirectorDTO movieDirectorDTO) {
        return new ResponseEntity<>(movieDirectorService.editMovieDirector(movieDirectorDTO), HttpStatus.OK);
    }

    //Delete a director
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteDirector(@PathVariable int id) {
        return new ResponseEntity<>(movieDirectorService.deleteMovieDirectorById(id), HttpStatus.OK);
    }
}
