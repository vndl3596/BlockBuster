package com.example.demo.controller;

import com.example.demo.DTO.*;
import com.example.demo.model.MovieDetail;
import com.example.demo.service.MovieDetailService;
import com.example.demo.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/movieDetail")
public class MovieDetailController {
    private final MovieDetailService movieDetailService;
    private final EntityManager entityManager;

    @GetMapping("/page")
    public MovieDetailPage getAllUsers(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize, @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy, @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return movieDetailService.getAllMovieDetailPage(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/getMovieDetail/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetailById(@PathVariable int id) {
        return new ResponseEntity<>(movieDetailService.getMovieById(id), HttpStatus.OK);
    }

    @GetMapping("/getMovieDetailAll")
    public ResponseEntity<List<MovieDetailDTO>> getMovieDetailAll() {
        return new ResponseEntity<>(movieDetailService.getAllMovie(), HttpStatus.OK);
    }

    @GetMapping("/getMovieRates")
    public ResponseEntity<List<MovieRate>> getMovierates() {
        return new ResponseEntity<>(movieDetailService.getListMovieRate(), HttpStatus.OK);
    }

    @GetMapping("/getMovieRate/{id}")
    public ResponseEntity<Float> getMovierate(@PathVariable int id) {
        return new ResponseEntity<>(movieDetailService.getRateMovie(id), HttpStatus.OK);
    }

    @GetMapping("/getGenreByMovieId/{id}")
    public ResponseEntity<List<MovieGenreDTO>> getGenreByMovieId(@PathVariable int id) {
        return new ResponseEntity<>(movieDetailService.getMovieGenres(id), HttpStatus.OK);
    }

    @GetMapping("/getCastByMovieId/{id}")
    public ResponseEntity<List<MovieCastDTO>> getCastByMovieId(@PathVariable int id) {
        return new ResponseEntity<>(movieDetailService.getMovieCasts(id), HttpStatus.OK);
    }

    @GetMapping("/getMovieDetailByTitle/{title}")
    public ResponseEntity<MovieDetail> getMovieDetailByTitle(@PathVariable String title) {
        return new ResponseEntity<>(movieDetailService.getMovieDetailByTitle(title), HttpStatus.OK);
    }

    @PostMapping("/addMovie")
    public ResponseEntity<MovieDetailDTO> addMovieDetail(@RequestBody MovieDetail movieDetailDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.addMovieDetail(movieDetailDTO), HttpStatus.OK);
    }

    @PutMapping("/editMovieDetail")
    public ResponseEntity<MovieDetail> editMovieDetail(@RequestBody MovieDetail movieDetailDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.editMovieDetail(movieDetailDTO), HttpStatus.OK);
    }

    @PutMapping("/editMovie")
    public ResponseEntity<MovieDetailDTO> editMovie(@RequestBody MovieDetailDTO movieDetailDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.editMovie(movieDetailDTO), HttpStatus.OK);
    }
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<MovieDetail> removeMovie(@PathVariable int id) throws Exception {
        return new ResponseEntity<>(movieDetailService.deleteMovieDetail(id), HttpStatus.OK);
    }

    @GetMapping("/search/{searchKey}")
    public ResponseEntity<List<MovieDetailDTO>> fullTextSearch(@PathVariable String searchKey) {
        String searchQuery = "%" + searchKey + "%";
        return new ResponseEntity<>(movieDetailService.search(searchQuery), HttpStatus.OK);

    }

    @GetMapping("/loadEvaluateInMovie/{idMovie}")
    public ResponseEntity<List<MovieEvaluateDTO>> loadEvaluateInMovie(@PathVariable("idMovie") int movieId) {
        return new ResponseEntity<>(movieDetailService.loadEvaluate(movieId), HttpStatus.OK);
    }

    @GetMapping("/loadEvaluateInAccount/{idAcc}")
    public ResponseEntity<List<MovieEvaluateDTO>> loadEvaluateInAccount(@PathVariable("idAcc") int accId) {
        return new ResponseEntity<>(movieDetailService.loadEvaluateInAcc(accId), HttpStatus.OK);
    }

    @PutMapping("/saveEvaluate")
    public ResponseEntity<MovieEvaluateDTO> saveEvaluate(@RequestBody MovieEvaluateDTO movieEvaluateDTO) throws Exception {
        return new ResponseEntity<>(movieDetailService.saveEvaluate(movieEvaluateDTO), HttpStatus.OK);
    }

}
