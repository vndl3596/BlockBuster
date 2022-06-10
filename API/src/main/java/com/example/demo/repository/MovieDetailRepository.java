package com.example.demo.repository;

import com.example.demo.model.MovieDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieDetailRepository extends JpaRepository<MovieDetail, Integer> {
    MovieDetail findMovieDetailByTitle(String title);
    List<MovieDetail> findByTitleLike(String title);

}
