package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.MovieDetail;
import com.example.demo.service.MovieDetailService;
import com.example.demo.service.MovieEvaluateService;
import com.example.demo.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/movieDetail")
public class MovieDetailController {
    private final MovieDetailService movieDetailService;
    private final MovieEvaluateService movieEvaluateService;
    private final EntityManager entityManager;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/getMovieDetail/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetailById(@PathVariable int id) {
        return new ResponseEntity<>(movieDetailService.getMovieById(id), HttpStatus.OK);
    }

    @GetMapping("/getMovieDetailAll")
    public ResponseEntity<List<MovieDetailDTO>> getMovieDetailAll() {
        return new ResponseEntity<>(movieDetailService.getAllMovie(), HttpStatus.OK);
    }

    @GetMapping("/getMovieShowinglAll")
    public ResponseEntity<List<MovieDetailDTO>> getMovieShowinglAll() {
        return new ResponseEntity<>(movieDetailService.getAllShowingMovie(), HttpStatus.OK);
    }

    @GetMapping("/getMovieCominglAll")
    public ResponseEntity<List<MovieDetailDTO>> getMovieCominglAll() {
        return new ResponseEntity<>(movieDetailService.getAllCommingMovie(), HttpStatus.OK);
    }

    @GetMapping("/getMovieRate/{id}")
    public ResponseEntity<MovieRate> getMovierate(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(movieDetailService.getRateMovie(id), HttpStatus.OK);
    }

    @PostMapping("/addMovie")
    public ResponseEntity<MovieDetailDTO> addMovieDetail(@RequestBody MovieDetailDTO movieDetailDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.addMovieDetail(movieDetailDTO), HttpStatus.OK);
    }

    @GetMapping("/addView/{id}")
    public ResponseEntity<Integer> addMovieDetail(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(movieDetailService.addView(id), HttpStatus.OK);
    }

    @PostMapping("/editMovieDetail")
    public ResponseEntity<MovieDetailDTO> editMovieDetail(@RequestBody MovieDetailDTO movieDetailDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.editMovieDetail(movieDetailDTO), HttpStatus.OK);
    }

    @PutMapping("/saveEvaluate")
    public ResponseEntity<MovieEvaluateDTO> saveEvaluate(@RequestBody MovieEvaluateDTO movieEvaluateDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.saveEvaluate(movieEvaluateDTO), HttpStatus.OK);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<String> removeMovie(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(movieDetailService.deleteMovieDetail(id), HttpStatus.OK);
    }

    @GetMapping("/loadEvaluateInMovie/{idMovie}")
    public ResponseEntity<List<MovieEvaluateDTO>> loadEvaluateInMovie(@PathVariable("idMovie") int movieId) {
        return new ResponseEntity<>(movieDetailService.loadEvaluate(movieId), HttpStatus.OK);
    }

    @GetMapping("/loadEvaluateInAccount/{idAcc}")
    public ResponseEntity<List<MovieEvaluateDTO>> loadEvaluateInAccount(@PathVariable("idAcc") int accId) {
        return new ResponseEntity<>(movieDetailService.loadEvaluateInAcc(accId), HttpStatus.OK);
    }
    @GetMapping("/disableEvaluate/idMv={movieId}&idA={accountId}")
    public ResponseEntity<MovieEvaluateDTO> disableEvaluateInAccount(@PathVariable("movieId") int movieId, @PathVariable("accountId") int accountId) {
        return new ResponseEntity<>(movieEvaluateService.disableEvaluate(movieId, accountId), HttpStatus.OK);
    }
}
