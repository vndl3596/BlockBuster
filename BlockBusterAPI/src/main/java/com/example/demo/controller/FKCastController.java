package com.example.demo.controller;

import com.example.demo.dto.FKCastDTO;
import com.example.demo.dto.MovieDetailDTO;
import com.example.demo.service.FKCastService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<FKCastDTO>> getAllCastOnAMovie(@PathVariable int movieId) {
        return new ResponseEntity<>(fkCastService.getCastByMovieId(movieId), HttpStatus.OK);
    }

    @PostMapping("/create/idMv={movieId}&idA={actorId}")
    public ResponseEntity<String> addActorOnMovie(@PathVariable int movieId, @PathVariable int actorId, @RequestBody String character){
        return new ResponseEntity<>(fkCastService.addActorForMovie(movieId, actorId, character), HttpStatus.OK);
    }
    @PostMapping("/edit/idMv={movieId}&idA={actorId}")
    public ResponseEntity<String> editActorOnMovie(@PathVariable int movieId, @PathVariable int actorId, @RequestBody String character){
        return new ResponseEntity<>(fkCastService.editActorForMovie(movieId, actorId, character), HttpStatus.OK);
    }
    @GetMapping("/remove/idMv={movieId}&idA={actorId}")
    public ResponseEntity<String> deleteActorOnMovie(@PathVariable int movieId, @PathVariable int actorId){
        return new ResponseEntity<>(fkCastService.deleteActorForMovie(movieId, actorId), HttpStatus.OK);
    }
}
