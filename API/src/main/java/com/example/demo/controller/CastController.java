package com.example.demo.controller;

import com.example.demo.DTO.CastPage;
import com.example.demo.DTO.MovieCastDTO;
import com.example.demo.service.MovieCastService;
import com.example.demo.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cast")
public class CastController {
    private final MovieCastService movieCastService;

    //get page cast
    @GetMapping("/page")
    public CastPage getAllUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return movieCastService.getAllCastPage(pageNo, pageSize, sortBy, sortDir);
    }

    //get all cast
    @GetMapping("/getAll")
    public ResponseEntity<List<MovieCastDTO>> getAllCast() {
        return new ResponseEntity<>(movieCastService.getAllCast(), HttpStatus.OK);
    }

    //get cast by id
    @GetMapping("/{id}")
    public ResponseEntity<MovieCastDTO> getCastById(@PathVariable int id) {
        return new ResponseEntity<>(movieCastService.getMovieCastById(id), HttpStatus.OK);
    }

    //create a cast
    @PostMapping("/create")
    public ResponseEntity<MovieCastDTO> createCast(@RequestBody MovieCastDTO movieCastDTO) {
        return new ResponseEntity<>(movieCastService.createMovieCast(movieCastDTO), HttpStatus.OK);
    }

    //edit cast
    @PutMapping("/edit")
    public ResponseEntity<String> editCast(@RequestBody MovieCastDTO movieCastDTO){
        return new ResponseEntity<>(movieCastService.editMovieCast(movieCastDTO), HttpStatus.OK);
    }

    //delete cats
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCastById(@PathVariable int id){
        return new ResponseEntity<>(movieCastService.deleteMovieCastById(id),HttpStatus.OK);
    }
}
